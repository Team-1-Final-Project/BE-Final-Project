package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import lombok.Getter;

@Getter

public class CrewResponseDto {

  private final Long memberId;
  private final Long meetingId;
  private final String nickname;

  public CrewResponseDto(Crew crew){
    this.memberId = crew.getMember().getId();
    this.meetingId = crew.getMeeting().getId();
    this.nickname = crew.getMember().getNickname();
  }

}
