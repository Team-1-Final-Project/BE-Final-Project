package com.innovation.backend.controller;

import com.innovation.backend.dto.request.MeetingRequestDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.service.MeetingService;
import com.innovation.backend.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MeetingController {

  private final MemberService memberService;

  private final MeetingService meetingService;


 // 모임 생성
  @PostMapping("/meeting")
  public ResponseDto<?> createMeeting(@AuthenticationPrincipal UserDetails userDetails,@RequestPart("data") MeetingRequestDto requestDto,@RequestPart(required = false) MultipartFile image
  ) {
    try {
      Member member = memberService.memberFromUserDetails(userDetails);
      meetingService.createMeeting(requestDto, member,image);
    } catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임이 생성되었습니다.");
  }




  //모임 수정

  @PutMapping("/meeting/{meetingId}")
  public ResponseDto<?> updateMeeting(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("meetingId") Long meetingId, @RequestBody MeetingRequestDto requestDto){

    try{
      Member member = memberService.memberFromUserDetails(userDetails);
      meetingService.updateMeeting(meetingId,requestDto,member);
    } catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임이 수정되었습니다.");
  }

  //모임 이미지 수정
  @PutMapping("/meeting/{meetingId}/image")
  public ResponseDto<?> updateMeetingImage(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("meetingId") Long meetingId,@RequestPart(required = false) MultipartFile image){
    Member member = memberService.memberFromUserDetails(userDetails);
    try{
      meetingService.updateMeetingImage(meetingId,image);
    } catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임 사진이 수정되었습니다.");

  }

  //모임 이미지 삭제
  @DeleteMapping("/meeting/{meetingId}/image")
  public ResponseDto<?> deleteMeetingImage(@AuthenticationPrincipal UserDetails userDetails,@PathVariable("meetingId") Long meetingId){

    try{
      Member member = memberService.memberFromUserDetails(userDetails);
      meetingService.deleteImage(meetingId,member);
    } catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임 사진이 수정되었습니다.");

  }




  //모임 삭제
  @DeleteMapping("/meeting/{meetingId}")
  public ResponseDto<?> deleteMeeting(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("meetingId") Long meetingId){
    try{
      //로그인한 사용자인지 확인
      Member member = memberService.memberFromUserDetails(userDetails);

      meetingService.deleteMeeting(meetingId,member);
    }catch (Exception e) {
      log.error(e.getMessage());
      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임을 삭제하였습니다.");

  }

  //모임 전체 조회
//  @GetMapping("/meeting")
//  public ResponseDto<List<MeetingResponseDto>> getAllMeeting (){
//    List<MeetingResponseDto> meetingResponseDtoList;
//
//    try {
//      meetingResponseDtoList = meetingService.getAllMeeting();
//    }catch (Exception e){
//      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
//    }
//    return ResponseDto.success(meetingResponseDtoList);
//  }

  //모임 상세 조회

  //모임 태그별 조회?

  //모임 좋아요/취소

}
