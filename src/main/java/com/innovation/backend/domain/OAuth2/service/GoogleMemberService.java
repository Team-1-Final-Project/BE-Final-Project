package com.innovation.backend.domain.OAuth2.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.innovation.backend.domain.OAuth2.dto.GoogleLoginDto;
import com.innovation.backend.domain.OAuth2.vo.GoogleLoginRequestVo;
import com.innovation.backend.domain.OAuth2.vo.GoogleLoginResponseVo;
import com.innovation.backend.global.util.GoogleConfigUtils;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.Authority;
import com.innovation.backend.security.jwt.TokenDto;
import com.innovation.backend.security.jwt.TokenProvider;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleMemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final GoogleConfigUtils googleConfigUtils;
    private final TokenProvider tokenProvider;

    public RedirectView googleLogin(String authCode, HttpServletResponse response) throws JsonProcessingException {
        GoogleLoginDto userInfo = getGoogleUserInfo(authCode);

        Member googleUser = signupGoogleUserIfNeeded(userInfo);

        forceLogin(googleUser);

        TokenDto tokenDto = tokenProvider.generateTokenDto(googleUser);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:3000");
        return redirectView;

    }


    private GoogleLoginDto getGoogleUserInfo(String authCode) throws JsonProcessingException {
        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        GoogleLoginRequestVo requestParams = GoogleLoginRequestVo.builder()
                .clientId(googleConfigUtils.getGoogleClientId())
                .clientSecret(googleConfigUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(googleConfigUtils.getGoogleRedirectUri()) // 오류시
                .grantType("authorization_code")
                .build();

        // Http Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleLoginRequestVo> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(googleConfigUtils.getGoogleAuthUrl() + "/token", httpRequestEntity, String.class);

        // ObjectMapper를 통해 String to Object로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
        GoogleLoginResponseVo googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleLoginResponseVo>() {});

        // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
        String jwtToken = googleLoginResponse.getIdToken();

        // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleConfigUtils.getGoogleAuthUrl() + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

        String resultJson = restTemplate.getForObject(requestUrl, String.class);
        GoogleLoginDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<GoogleLoginDto>() {});

        return userInfoDto;
    }

    private Member signupGoogleUserIfNeeded(GoogleLoginDto userInfo) {
        String email = userInfo.getEmail();
        Member googleUser = memberRepository.findByEmail(email).orElse(null);

        if (googleUser == null) { // 회원가입
            String nickname = userInfo.getName();

            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);
            String profileImage = userInfo.getPicture();

            Authority authority = Authority.valueOf("ROLE_USER");
            String provider = "google";


            googleUser = new Member(email, nickname, encodedPassword, profileImage, authority, provider);
            memberRepository.save(googleUser);
        }
        return googleUser;
    }

    private void forceLogin(Member googleUser) {
        UserDetails userDetails = new UserDetailsImpl(googleUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
