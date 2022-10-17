package com.innovation.backend.domain.Member.controller;

import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.DailyMissionResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.MissionClearResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.service.MyPageService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.domain.Review.dto.ReviewResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MyPageController {
  private final MyPageService myPageService;

  //참여한 모임 조회
  @GetMapping("/mypage/meeting")
  public ResponseDto<List<MeetingResponseDto>> getJoinMeeting (@AuthenticationPrincipal UserDetailsImpl userDetails){
    return ResponseDto.success(myPageService.GetJoinMeeting(userDetails));
  }

  //내가 작성한 후기 조회
  @GetMapping("/mypage/review")
  public ResponseDto<List<ReviewResponseDto>> getMyReview (@AuthenticationPrincipal UserDetailsImpl userDetails){
    return ResponseDto.success(myPageService.getMyReview(userDetails));
  }

  //작성한 게시글 조회
  @GetMapping("/mypage/board")
  public ResponseDto<List<BoardResponseDto>> getWriteBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<BoardResponseDto> boardResponseDtoList;
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
  public ResponseDto<List<BoardResponseDto>> getMyHitBoard(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<BoardResponseDto> boardResponseDtoList;
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
}


