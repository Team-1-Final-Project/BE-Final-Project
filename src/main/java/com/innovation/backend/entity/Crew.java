package com.innovation.backend.entity;

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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Crew extends Timestamped{

  //참여자 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //참여 여부
//  @Column
//  private boolean attendance;

  //유저 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEMBER_ID",  nullable = false)
  private Member member;

  //모임 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEETING_ID")
  private Meeting meeting;

  //모임에 크루 참여
  public Crew(Member member, Meeting meeting) {
    this.member = member;
    this.meeting = meeting;
//    this.attendance = false;
  }

  //참여시 참여상태 변경
//  public void attend() {
//    this.attendance = true;
//  }

}