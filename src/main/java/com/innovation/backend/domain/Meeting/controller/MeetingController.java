package com.innovation.backend.domain.Meeting.controller;

import com.innovation.backend.domain.Badge.service.BadgeService;
import com.innovation.backend.domain.Meeting.dto.response.MeetingGetAllResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingLikeResponseDto;
import com.innovation.backend.domain.Meeting.dto.request.MeetingRequestDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Meeting.service.MeetingService;
import com.innovation.backend.domain.Meeting.dto.request.TagMeetingRequestDto;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
  private final BadgeService badgeService;


  // 모임 생성
  @PostMapping("/meeting")
  public ResponseDto<MeetingResponseDto> createMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart("data") MeetingRequestDto requestDto, @RequestPart(required = false) MultipartFile image) {
    badgeService.getWelcomeMeetingBadge(userDetails, "모임 개척자");
    return ResponseDto.success(meetingService.createMeeting(requestDto, userDetails, image));
  }


  //모임 수정
  @PutMapping("/meeting/{meetingId}")
  public ResponseDto<String> updateMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable("meetingId") Long meetingId, @RequestPart("data") MeetingRequestDto requestDto,@RequestPart(required = false) MultipartFile image) {
      meetingService.updateMeeting(meetingId, requestDto, userDetails,image);
    return ResponseDto.success("모임이 수정되었습니다.");
  }


  //모임 삭제
  @DeleteMapping("/meeting/{meetingId}")
  public ResponseDto<String> deleteMeeting(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("meetingId") Long meetingId) {
      meetingService.deleteMeeting(meetingId, userDetails);
    return ResponseDto.success("모임을 삭제하였습니다.");

  }

  //모임 전체 조회
  @GetMapping("/meeting")
  public ResponseDto<Page<MeetingGetAllResponseDto>> getAllMeeting(@PageableDefault(size = 12) Pageable pageable) {
    return ResponseDto.success(meetingService.getAllMeeting(pageable));

  }

  //모임 상세 조회
  @GetMapping("/meeting/{meetingId}")
  public ResponseDto<MeetingResponseDto> getMeeting(@PathVariable("meetingId") Long meetingId) {
    return ResponseDto.success(meetingService.getMeeting(meetingId));
  }

  //모임 태그별 조회
  @PostMapping("/meeting/tag")
  public ResponseDto<Page<MeetingGetAllResponseDto>> getMeetingByTag(@RequestBody TagMeetingRequestDto tagIds, @PageableDefault(size = 12) Pageable pageable){
    return ResponseDto.success(meetingService.getMeetingByTag(tagIds, pageable));
  }

  //모임 좋아요
  @PutMapping("/meeting/heart/{meetingId}")
  public ResponseDto<MeetingLikeResponseDto> addMeetingLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long meetingId) {
    return ResponseDto.success(meetingService.addMeetingLike(userDetails,meetingId));
  }

  //모임 좋아요 여부확인
  @GetMapping("/meeting/heart/{meetingId}")
  public ResponseDto<MeetingLikeResponseDto> getMeetingLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long meetingId) {
    return ResponseDto.success(meetingService.getMeetingLike(userDetails,meetingId));
  }
}
