package com.innovation.backend.service;


import com.innovation.backend.dto.request.MemberRequestDto;
import com.innovation.backend.dto.response.MemberResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.Authority;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.jwt.TokenDto;
import com.innovation.backend.jwt.TokenProvider;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.MemberRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
//@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public ResponseDto<?> createMember(MemberRequestDto requestDto) {

        if(memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            return ResponseDto.fail(ErrorCode.DUPLICATED_EMAIL);
        }
        String[] nickname = requestDto.getEmail().split("@");

        Member member = Member.builder()        //provider <- null
                .email(requestDto.getEmail())
                .nickname(nickname[0])
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        memberRepository.save(member);

        return ResponseDto.success("회원가입 완료");
    }

    public ResponseDto<?> loginMember(MemberRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getEmail());

        if(null == member) {
            return ResponseDto.fail(ErrorCode.EMAIL_NOT_FOUND);
        }

        if(!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(ErrorCode.PASSWORDS_NOT_MATCHED);
        }

        putToken(member, response);

        MemberResponseDto responseDto = MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .authority(member.getAuthority()).build();

        return ResponseDto.success(responseDto);
    }

    public ResponseDto<?> logoutMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail(ErrorCode.BAD_TOKEN_REQUEST);
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        }

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public Member isPresentMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }
    @Transactional
    public void putToken(Member member, HttpServletResponse response) {
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

    // 다른사람 유저정보 확인
    public MemberResponseDto getUserInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        System.out.println(member);
        return new MemberResponseDto(member);
    }
}

