package com.innovation.backend.controller;

import com.innovation.backend.dto.response.CrewResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.service.CrewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CrewController {

  private final CrewService crewService;

  //모임 참여하기
  @PostMapping("/meeting/join/{meetingId}")
  public ResponseDto<String> joinMeeting (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("meetingId") Long meetingId){
    try{
      Member member = userDetails.getMember();
      crewService.join(meetingId,member.getId());
    }catch(CustomErrorException e) {
      log.error(e.getMessage());
      return ResponseDto.fail(e.getErrorCode());
    }catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임 참여 완료");
  }

  //모임 참여 취소하기
  @DeleteMapping ("/meeting/join/{meetingId}")
  public ResponseDto<String> cancelJoinMeeting (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("meetingId") Long meetingId){
    try{
      Member member = userDetails.getMember();
      crewService.cancelJoin(meetingId, member.getId());
    }catch(CustomErrorException e) {
      log.error(e.getMessage());
      return ResponseDto.fail(e.getErrorCode());
    }catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success("모임 참여 취소 완료");
  }

  //모임 참여 유저 조회 (전체)
  @GetMapping("/meeting/crew/{meetingId}")
  public ResponseDto<List<CrewResponseDto>> getCrewList ( @PathVariable("meetingId") Long meetingId){
    List<CrewResponseDto> crewResponseDtoList;
    try{
      crewResponseDtoList = crewService.getCrewList(meetingId);
    }catch(CustomErrorException e) {
      log.error(e.getMessage());
      return ResponseDto.fail(e.getErrorCode());
    }catch (Exception e) {
      log.error("error: ", e);
      return  ResponseDto.fail(ErrorCode.INVALID_ERROR);
    }
    return ResponseDto.success(crewResponseDtoList);
  }

}
