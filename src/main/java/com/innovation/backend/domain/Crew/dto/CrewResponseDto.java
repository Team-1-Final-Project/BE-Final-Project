package com.innovation.backend.domain.Crew.dto;

import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Member.dto.response.BadgeResponseDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
