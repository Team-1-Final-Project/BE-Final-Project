package com.innovation.backend.domain.Meeting.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagMeetingRequestDto {

  private final List<Long> tagIds;

}
