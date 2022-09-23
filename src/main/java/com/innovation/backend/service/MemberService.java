package com.innovation.backend.service;


import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.exception.ErrorCode;
import com.innovation.backend.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  //로그인한 사용자인지 확인
  public Member memberFromUserDetails(UserDetails userDetails) {
    if (userDetails instanceof UserDetailsImpl) {
      return ((UserDetailsImpl) userDetails).getUser();
    } else {
      throw new CustomErrorException(ErrorCode.NEED_LOGIN);
    }
  }
}
