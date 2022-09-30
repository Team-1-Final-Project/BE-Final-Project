package com.innovation.backend.controller;

import com.innovation.backend.dto.request.MeetingRequestDto;
import com.innovation.backend.dto.request.TagMeetingRequestDto;
import com.innovation.backend.dto.response.MeetingLikeResponseDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.MeetingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  private final MeetingService meetingService;



  // 모임 생성
  @PostMapping("/meeting")
  public ResponseDto<MeetingResponseDto> createMeeting(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestPart("data") MeetingRequestDto requestDto,
      @RequestPart(required = false) MultipartFile image) {
      Member member = userDetails.getMember();
    MeetingResponseDto meetingResponseDto = meetingService.createMeeting(requestDto, member, image);
    return ResponseDto.success(meetingResponseDto);
  }


  //모임 수정
  @PutMapping("/meeting/{meetingId}")
  public ResponseDto<String> updateMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("meetingId") Long meetingId, @RequestBody MeetingRequestDto requestDto) {
      Member member = userDetails.getMember();
      meetingService.updateMeeting(meetingId, requestDto, member);
    return ResponseDto.success("모임이 수정되었습니다.");
  }

  //모임 이미지 수정
  @PutMapping("/meeting/{meetingId}/image")
  public ResponseDto<String> updateMeetingImage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("meetingId") Long meetingId,
      @RequestPart(required = false) MultipartFile image) {
      Member member = userDetails.getMember();
      meetingService.updateMeetingImage(meetingId, member, image);
    return ResponseDto.success("모임 사진이 수정되었습니다.");

  }

  //모임 이미지 삭제
  @DeleteMapping("/meeting/{meetingId}/image")
  public ResponseDto<String> deleteMeetingImage(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("meetingId") Long meetingId) {
      Member member = userDetails.getMember();
      meetingService.deleteImage(meetingId, member);
    return ResponseDto.success("모임 사진이 삭제 되었습니다.");

  }


  //모임 삭제
  @DeleteMapping("/meeting/{meetingId}")
  public ResponseDto<String> deleteMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("meetingId") Long meetingId) {
      Member member = userDetails.getMember();
      meetingService.deleteMeeting(meetingId, member);
    return ResponseDto.success("모임을 삭제하였습니다.");

  }

  //모임 전체 조회
  @GetMapping("/meeting")
  public ResponseDto<List<MeetingResponseDto>> getAllMeeting() {
    List<MeetingResponseDto> meetingResponseDtoList = meetingService.getAllMeeting();
    return ResponseDto.success(meetingResponseDtoList);
  }

  //모임 상세 조회
  @GetMapping("/meeting/{meetingId}")
  public ResponseDto<MeetingResponseDto> getMeeting(@PathVariable("meetingId") Long meetingId) {
    MeetingResponseDto meetingResponseDto = meetingService.getMeeting(meetingId);
    return ResponseDto.success(meetingResponseDto);
  }

  //모임 태그별 조회
  @PostMapping("/meeting/tag")
  public ResponseDto<List<MeetingResponseDto>> getMeetingByTag(@RequestBody TagMeetingRequestDto tagIds){
    return ResponseDto.success(meetingService.getMeetingByTag(tagIds));
  }

  //모임 좋아요
  @PutMapping("/meeting/heart/{meetingId}")
  public ResponseDto<MeetingLikeResponseDto> addMeetingLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long meetingId) {

    return ResponseDto.success(meetingService.addMeetingLike(userDetails,meetingId));
  }

  //모임 좋아요 여부확인
  @GetMapping("/meeting/heart/{meetingId}")
  public ResponseDto<MeetingLikeResponseDto> getMeetingLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long meetingId) {
    MeetingLikeResponseDto meetingLikeResponseDto = meetingService.getMeetingLike(userDetails,meetingId);
    return ResponseDto.success(meetingLikeResponseDto);
  }
}
