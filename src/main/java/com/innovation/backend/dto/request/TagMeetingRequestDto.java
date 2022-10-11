package com.innovation.backend.dto.request;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagMeetingRequestDto {

  private List<Long> tagIds;

}
