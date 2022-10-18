package com.innovation.backend.domain.Crew.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.util.Timestamped;
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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Crew extends Timestamped {

  //참여자 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //유저 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID",  nullable = false)
  @JsonIgnore
  private Member member;

  //모임 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEETING_ID")
  @JsonIgnore
  private Meeting meeting;

  //모임에 크루 참여
  public Crew(Member member, Meeting meeting) {
    this.member = member;
    this.meeting = meeting;
  }
}
