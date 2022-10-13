package com.innovation.backend.domain.Review.domain;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Review.dto.ReviewRequestDto;
import com.innovation.backend.global.util.Timestamped;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends Timestamped {

  //후기 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //후기 내용
  @Column(nullable = false,columnDefinition = "TEXT")
  private String content;

  //후기 사진
  @Column
  private String reviewImage;
  //섬네일 사진
  private String reviewThumbImage;

  //모임
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id", nullable = false)
  private Meeting meeting;

  //작성자
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;


  //후기 생성
  public Review (ReviewRequestDto requestDto, Member member, Meeting meeting, String reviewImage, String reviewThumbImage){
    this.content = requestDto.getContent();
    this.reviewImage = reviewImage;
    this.reviewThumbImage = reviewThumbImage;
    this.member = member;
    this.meeting = meeting;
  }

  //후기 수정
public void updateReview(ReviewRequestDto requestDto,String reviewImage, String reviewThumbImage){
  this.content = requestDto.getContent();
  this.reviewImage = reviewImage;
  this.reviewThumbImage = reviewThumbImage;
}


}
