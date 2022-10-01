package com.innovation.backend.entity;

import com.innovation.backend.dto.request.ReviewRequestDto;
import java.security.PrivateKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review extends Timestamped{

  //후기 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //후기 내용
  @Column(nullable = false, length = 1000)
  private String content;

  //후기 사진
  @Column
  private String reviewImage;

  //모임
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id", nullable = false)
  private Meeting meeting;

  //작성자
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;


  //후기 생성
  public Review (ReviewRequestDto requestDto, Member member, Meeting meeting, String reviewImage){
    this.content = requestDto.getContent();
    this.reviewImage = reviewImage;
    this.member = member;
    this.meeting = meeting;
  }

  //후기 수정
public void updateReview(ReviewRequestDto requestDto,String reviewImage){
  this.content = requestDto.getContent();
  this.reviewImage = reviewImage;
}


}
