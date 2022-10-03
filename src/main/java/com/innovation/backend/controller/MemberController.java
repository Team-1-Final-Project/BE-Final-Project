package com.innovation.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innovation.backend.config.GoogleConfigUtils;
import com.innovation.backend.dto.response.MemberResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.service.GoogleMemberService;
import com.innovation.backend.entity.Member;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.service.KakaoMemberService;
import com.innovation.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final GoogleConfigUtils googleConfigUtils;
    private final GoogleMemberService googleMemberService;
    private final MemberService memberService;
    private final KakaoMemberService kakaoMemberService;
    private final MemberRepository memberRepository;


    @RequestMapping(value = "/login/google", method = RequestMethod.GET)
    public ResponseEntity<Object> moveGoogleInitUrl() {
        String authUrl = googleConfigUtils.googleInitUrl();
        URI redirectUri;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }


    @RequestMapping(value = "/login/oauth2/code/google", method = RequestMethod.GET)
    public RedirectView redirectGoogleLogin(@RequestParam(value = "code") String authCode, HttpServletResponse response) throws JsonProcessingException {
        return googleMemberService.googleLogin(authCode, response);
    }

    // 카카오 로그인
    @GetMapping("/login/kakao")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoMemberService.kakaoLogin(code, response);
    }

    // 로그인한 유저정보 가져오기
    @GetMapping(value = "/login/member", produces = "application/json; charset=UTF-8")
    public Optional<Member> getLoginInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String userId = userDetails.getUsername();
        Optional<Member> member = memberRepository.findByEmail(userId);
        return member;
    }

    // 다른사람 유저정보 확인
    @GetMapping("/member/{memberId}")
    public ResponseDto<MemberResponseDto> getUserInfo(@PathVariable Long memberId) {
        MemberResponseDto memberResponseDto;
        try {
            memberResponseDto = memberService.getUserInfo(memberId);
        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseDto.fail(ErrorCode.NOT_FOUND_USER);
        }
        return ResponseDto.success(memberResponseDto);
    }
}

