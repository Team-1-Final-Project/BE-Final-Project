package com.innovation.backend.jwt;

import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.RefreshToken;
import com.innovation.backend.enums.Authority;
import com.innovation.backend.enums.ErrorCode;

import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.repository.RefreshTokenRepository;
import com.innovation.backend.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60;            //1시간
    private static final long REFRESH_TOKEN_EXPRIRE_TIME = 1000 * 60 * 60 * 24 * 7;     //7일

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Member member) {
        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .claim(AUTHORITIES_KEY, Authority.ROLE_USER.toString())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                // 유효시간 저장
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPRIRE_TIME))
                // 사용할 암호화 알고리즘과 signature 에 들어갈 키값 세팅
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenObject = RefreshToken.builder()
                .id(member.getId())
                .member(member)
                .tokenValue(refreshToken)
                .build();

        refreshTokenRepository.save(refreshTokenObject);

        return TokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();

    }

    public Member getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new CustomErrorException(ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT token, 만료된 JWT token 입니다.");
            throw new CustomErrorException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new CustomErrorException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
//            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new CustomErrorException(ErrorCode.WRONG_TOKEN);
        }
    }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
        return optionalRefreshToken.orElse(null);
    }

    @Transactional
    public ResponseDto<?> deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        if (null == refreshToken) {
            return ResponseDto.fail(ErrorCode.TOKEN_NOT_FOUND);
        }
        refreshTokenRepository.delete(refreshToken);
        return ResponseDto.success("로그아웃이 성공적으로 완료되었습니다.");
    }
}
