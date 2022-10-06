package com.innovation.backend.domain.OAuth2.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponseVo {
    private String accessToken; // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    private String expiresIn;   // Access Token의 남은 수명
    private String refreshToken;
    private String scope;
    private String tokenType;   // 반환된 토큰 유형(Bearer 고정)
    private String idToken;
}
