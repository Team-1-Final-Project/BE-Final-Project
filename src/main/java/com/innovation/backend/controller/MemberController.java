package com.innovation.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.innovation.backend.entity.Member;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.service.KakaoMemberService;
import com.innovation.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoMemberService kakaoMemberService;
    private final MemberRepository memberRepository;

    // 카카오 로그인
    @GetMapping("/login/kakao")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoMemberService.kakaoLogin(code, response);
    }

    // 로그인한 유저정보 가져오기
    @GetMapping(value = "/login/member", produces = "application/json; charset=UTF-8")
    public Optional<Member> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        String userId = userDetails.getUsername();
        Optional<Member> member = memberRepository.findByEmail(userId);
        return member;
    }


}
