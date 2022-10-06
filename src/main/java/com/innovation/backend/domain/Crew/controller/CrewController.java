package com.innovation.backend.domain.Crew.controller;

import com.innovation.backend.domain.Crew.dto.CrewResponseDto;
import com.innovation.backend.domain.Crew.service.CrewService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
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
      crewService.join(meetingId,userDetails.getMember().getId());
    return ResponseDto.success("모임 참여 완료");
  }

  //모임 참여 취소하기
  @DeleteMapping ("/meeting/join/{meetingId}")
  public ResponseDto<String> cancelJoinMeeting (@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("meetingId") Long meetingId){
      crewService.cancelJoin(meetingId, userDetails.getMember().getId());
    return ResponseDto.success("모임 참여 취소 완료");
  }

  //모임 참여 유저 조회 (전체)
  @GetMapping("/meeting/crew/{meetingId}")
  public ResponseDto<List<CrewResponseDto>> getCrewList ( @PathVariable("meetingId") Long meetingId){
    return ResponseDto.success(crewService.getCrewList(meetingId));
  }
}
