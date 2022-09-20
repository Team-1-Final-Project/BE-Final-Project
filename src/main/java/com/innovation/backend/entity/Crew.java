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

  //출석 여부
  @Column
  private boolean attendance;

  //유저 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID",  nullable = false)
  private Member memberJoin;

  //모임 정보
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEETING_ID")
  private Meeting meetingJoin;


}
