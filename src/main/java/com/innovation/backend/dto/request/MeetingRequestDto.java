package com.innovation.backend.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MeetingRequestDto {

  private final String title;
  private final String content;
  private final String meetingImage;
  private final LocalDate joinStartDate;
  private final LocalDate joinEndDate;
  private final LocalDate meetingStartDate;
  private final LocalDate meetingEndDate;
  private final String location;
  private final int limitPeople;
  private final List<Long> tagMeetingIds;


}
