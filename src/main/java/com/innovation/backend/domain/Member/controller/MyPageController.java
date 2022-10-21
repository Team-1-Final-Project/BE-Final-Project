package com.innovation.backend.domain.Member.controller;


import com.innovation.backend.domain.Badge.dto.SignatureBadgeRequestDto;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.MainBoardResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.DailyMissionResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.MissionClearResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.dto.request.UserProfileRequestDto;
import com.innovation.backend.domain.Member.dto.request.UsernameRequestDto;
import com.innovation.backend.domain.Member.dto.response.BadgeResponseDto;
import com.innovation.backend.domain.Member.service.MyPageService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyPageController {
  private final MyPageService myPageService;

  //참여한 모임 조회
  @GetMapping("/mypage/meeting")
  public ResponseDto<List<MeetingResponseDto>> getJoinMeeting (@AuthenticationPrincipal UserDetailsImpl userDetails){
    List<MeetingResponseDto> meetingResponseDtoList;
    try {
      Member member = userDetails.getMember();
      meetingResponseDtoList = myPageService.GetJoinMeeting(member);
    } catch (CustomErrorException e) {
      log.error(e.getMessage());
      return ResponseDto.fail(e.getErrorCode());
    } catch (Exception e) {
      log.error("error: ", e);
      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success(meetingResponseDtoList);
  }

  //작성한 게시글 조회
  @GetMapping("/mypage/board")
  public ResponseDto<List<MainBoardResponseDto>> getWriteBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<MainBoardResponseDto> boardResponseDtoList;
    try {
      Member member = userDetails.getMember();
      boardResponseDtoList = myPageService.getWriteBoard(member);
    } catch (Exception e) {
      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success(boardResponseDtoList);
  }

  //좋아요한 게시글 조회
  @GetMapping("/mypage/hitboard")
  public ResponseDto<List<MainBoardResponseDto>> getMyHitBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<MainBoardResponseDto> boardResponseDtoList;
    try {
      Member member = userDetails.getMember();
      boardResponseDtoList = myPageService.getMyHitBoard(member);
    } catch (Exception e) {
      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success(boardResponseDtoList);
  }

  //완료한 미션 조회
  @GetMapping("/mypage/daily")
  public ResponseDto<List<MissionClearResponseDto>> getSuccessMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<MissionClearResponseDto> missionClearResponseDtoList;
    try {
      Member member = userDetails.getMember();
      missionClearResponseDtoList = myPageService.getSuccessMission(member);
    } catch (Exception e) {
      return ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success(missionClearResponseDtoList);
  }

  //획득한 배지 조회
  @GetMapping("/mypage/badge")
  public ResponseDto<List<BadgeResponseDto>> getMyBadge(@AuthenticationPrincipal UserDetailsImpl userDetails){
    Member member = userDetails.getMember();
    List<BadgeResponseDto> badgeResponseDtoList;
    badgeResponseDtoList = myPageService.getMyBadge(member);
    return ResponseDto.success((badgeResponseDtoList));
  }

  // 프로필 사진 설정
  @PostMapping("/mypage/profile")
  public ResponseDto<String> setUserprofile(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestPart(value = "profileImage") MultipartFile profileImage) {
    myPageService.setUserprofile(userDetails,profileImage);
    return ResponseDto.success("프로필 사진이 변경 되었습니다.");
  }

  // 대표 뱃지 설정
  @PostMapping("/mypage/badge")
  public ResponseDto<String> setSignatureBadge(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody SignatureBadgeRequestDto badgeId){
    myPageService.setSignatureBadge(userDetails,badgeId);
    return ResponseDto.success("대표 뱃지가 변경 되었습니다.");
  }
}