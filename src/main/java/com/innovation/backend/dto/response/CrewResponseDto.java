package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Crew;
import lombok.Getter;

@Getter

public class CrewResponseDto {
  private final Long crewId;

  private final Long memberId;
  private final Long meetingId;
  private final String email;
  private final String nickname;

  public CrewResponseDto(Crew crew){
    this.crewId = crew.getId();
    this.memberId = crew.getMember().getId();
    this.meetingId = crew.getMeeting().getId();
    this.email = crew.getMember().getEmail();
    this.nickname = crew.getMember().getNickname();
  }

}
