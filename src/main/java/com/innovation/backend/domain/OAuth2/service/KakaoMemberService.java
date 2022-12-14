package com.innovation.backend.domain.OAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.OAuth2.dto.KakaoUserInfoDto;
import com.innovation.backend.global.enums.Authority;
import com.innovation.backend.security.jwt.TokenDto;
import com.innovation.backend.security.jwt.TokenProvider;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        // 1. 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken(code);

        // 2. 토큰으로 유저정보 요청
        KakaoUserInfoDto kakaoUserInfoDto = getKakaoUserInfo(accessToken);

        // 3. 신규면 회원가입
        Member kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfoDto);

        // 4. 요청받은 유저정보 Jwt 토큰으로 생성후 헤더에 삽입
        TokenDto tokenDto = tokenProvider.generateTokenDto(kakaoUser);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());

        // 5. 소셜 로그인 처리
        forceLogin(kakaoUser);

    }

    // 1. 인가 코드로 액세스 토큰 요청
    private String getAccessToken(String code) throws JsonProcessingException {

        // developer kakao docs의 요구사항에 맞게 넣어줌
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "b6c7210fad39ab98eeed60425f16c67d");
        body.add("redirect_uri", "https://www.earthus.net/login/kakao");
        body.add("code", code);
        System.out.println(body);

        // HTTP 요청
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        System.out.println(rt);
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 유저정보 요청
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 동의내역 받아오기
        HttpEntity<MultiValueMap<String, String>> kakaoAgreedRequest = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> kakaoAgreedResponse = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/scopes",
                HttpMethod.GET,
                kakaoAgreedRequest,
                String.class
        );

        String kakaoAgreedResponseBody = kakaoAgreedResponse.getBody();
        ObjectMapper om = new ObjectMapper();
        JsonNode jn = om.readTree(kakaoAgreedResponseBody);
        String agreed = jn.get("scopes").get(1)
                          .get("agreed").asText();

        // 유저정보 받아오기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        // 기본 프로필 이미지
        String profileImage ="https://test-bucket-jaewon.s3.ap-northeast-2.amazonaws.com/images/basic.jpg";
        // 정보제공 동의 했을경우 이미지 삽입
        if (agreed == "true" ) {
            profileImage = jsonNode.get("properties")
                    .get("profile_image").asText();
        }

        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KakaoUserInfoDto(id, email, nickname,profileImage);
    }

    private Member registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfoDto) {
        String email = kakaoUserInfoDto.getEmail();

        Member kakaoUser = memberRepository.findByEmail(email).orElse(null);
        if (kakaoUser == null) {

            // 신규 회원가입
            // password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            Authority authority = Authority.valueOf("ROLE_USER");
            String provider = "kakao";
            String profileImage = kakaoUserInfoDto.getProfileImage();
            String nickname = kakaoUserInfoDto.getNickname();
            kakaoUser = new Member(email, nickname, encodedPassword, profileImage, authority, provider);

        }
        memberRepository.save(kakaoUser);
        return kakaoUser;
    }

    private void forceLogin(Member kakaoUser) {
        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
