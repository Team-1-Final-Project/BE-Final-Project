package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

  private final Long id;
  private final String content;
  private final String reviewImage;
  private final MemberResponseDto author;
  private final Long meetingId;

  public ReviewResponseDto(Review review){
    this.id = review.getId();
    this.content = review.getContent();
    this.reviewImage = review.getReviewImage();
    this.author = new MemberResponseDto(review.getMember());
    this.meetingId = review.getMeeting().getId();
  }

}
