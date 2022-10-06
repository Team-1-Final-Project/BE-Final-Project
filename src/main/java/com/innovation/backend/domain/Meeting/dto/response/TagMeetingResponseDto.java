package com.innovation.backend.domain.Meeting.dto.response;

import com.innovation.backend.domain.Meeting.domain.TagMeeting;
import lombok.Getter;

@Getter
public class TagMeetingResponseDto {
  private final Long id;
  private final String name;

  public TagMeetingResponseDto(TagMeeting tagMeeting){
    this.id = tagMeeting.getId();
    this.name = tagMeeting.getName();
  }
}
