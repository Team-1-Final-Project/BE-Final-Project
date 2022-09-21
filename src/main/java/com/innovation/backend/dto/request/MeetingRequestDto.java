package com.innovation.backend.dto.request;

import com.innovation.backend.entity.TagMeeting;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MeetingRequestDto {

  private final String title;
  private final String content;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalDateTime meetingDate;
  private final LocalDateTime meetingEndDate;
  private final String location;
  private final boolean online;
  private final int limitPeople;
  private final TagMeeting tag;
  private final String postImg;

}
