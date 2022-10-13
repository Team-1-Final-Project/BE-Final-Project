package com.innovation.backend.domain.Review.dto;

import com.innovation.backend.domain.Member.dto.response.MemberResponseDto;
import com.innovation.backend.domain.Review.domain.Review;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

  private final Long id;
  private final String content;
  private final String reviewImage;
  private final String reviewThumbImage;
  private final MemberResponseDto author;
  private final Long meetingId;
  private final LocalDateTime createdAt;

  public ReviewResponseDto(Review review){
    this.id = review.getId();
    this.content = review.getContent();
    this.reviewImage = review.getReviewImage();
    this.reviewThumbImage = review.getReviewThumbImage();
    this.author = new MemberResponseDto(review.getMember());
    this.meetingId = review.getMeeting().getId();
    this.createdAt = review.getCreatedAt();
  }

}
