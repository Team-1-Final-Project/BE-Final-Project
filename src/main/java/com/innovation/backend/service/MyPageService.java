package com.innovation.backend.service;

import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.repository.CrewRepository;
import com.innovation.backend.repository.MeetingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MyPageService {

  private final CrewRepository crewRepository;
  private final MeetingRepository meetingRepository;

  //참여한 모임 조회
  public List<MeetingResponseDto> GetJoinMeeting (Member member){

    List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();
    List<Crew> crewList = crewRepository.findByMember(member);

    for(Crew crew : crewList) {
      Meeting meeting = meetingRepository.findById(crew.getMeeting().getId())
          .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

      MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
      meetingResponseDtoList.add(meetingResponseDto);
    }

    return meetingResponseDtoList;
  }

}
