package com.innovation.backend.domain.Crew.dto;

import com.innovation.backend.domain.Crew.domain.Crew;
import lombok.Getter;

@Getter

public class CrewResponseDto {


  private final Long memberId;
  private final Long meetingId;
  private final String email;
  private final String nickname;
  private final String profileImage;

  public CrewResponseDto(Crew crew){

    this.memberId = crew.getMember().getId();
    this.meetingId = crew.getMeeting().getId();
    this.email = crew.getMember().getEmail();
    this.nickname = crew.getMember().getNickname();
    this.profileImage = crew.getMember().getProfileImage();
  }

}
