package com.innovation.backend.controller;

import com.innovation.backend.dto.response.BoardResponseDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.MyPageService;
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
}


