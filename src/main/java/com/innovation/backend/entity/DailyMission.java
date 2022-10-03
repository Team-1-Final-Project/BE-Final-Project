package com.innovation.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DailyMission extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String mission;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  public DailyMission(String mission, Member member) {
    this.mission = mission;
    this.member = member;
  }

//  public void count(Long clearCount) {
//    this.clearCount = clearCount;
//  }
}
