package com.innovation.backend.controller;

import com.innovation.backend.dto.request.MeetingRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.service.MeetingService;
import com.innovation.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MeetingController {

  private final MemberService memberService;

  private final MeetingService meetingService;


 // 모임 생성
  @PostMapping("/meeting")
  public ResponseDto<?> createMeeting(@AuthenticationPrincipal UserDetails userDetails, @RequestBody MeetingRequestDto requestDto
  ) {
    //로그인한 사용자인지 확인
    Member member = memberService.memberFromUserDetails(userDetails);

    //관리자 권한 있는지 체크 / 굳이?
//    memberService.userCheckEnumRole(member);

    meetingService.createMeeting(requestDto, member);
    return ResponseDto.success("모임이 생성되었습니다.");
  }




  //모임 수정

  @PutMapping("/meeting/{meetingId}")
  public ResponseDto<?> updateMeeting(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("meetingId") Long meetingId, @RequestBody MeetingRequestDto requestDto){

    //로그인한 사용자인지 확인
    Member member = memberService.memberFromUserDetails(userDetails);

    meetingService.updateMeeting(meetingId,requestDto,member);

    return ResponseDto.success("모임이 수정되었습니다.");
  }



  //모임 삭제

  @DeleteMapping("/meeting/{meetingId}")
  public ResponseDto<?> deleteMeeting(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("meetingId") Long meetingId){

    //로그인한 사용자인지 확인
    Member member = memberService.memberFromUserDetails(userDetails);

    meetingService.deleteMeeting(meetingId,member);

    return ResponseDto.success("모임을 삭제하였습니다.");

  }

  //모임 전체 조회

  //모임 상세 조회

  //모임 태그별 조회?

  //모임 좋아요/취소
  
}
