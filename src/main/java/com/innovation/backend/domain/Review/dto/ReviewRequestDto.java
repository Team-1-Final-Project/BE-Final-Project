package com.innovation.backend.domain.Review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewRequestDto {

  private final String content;
  private final String reviewImage;

}
