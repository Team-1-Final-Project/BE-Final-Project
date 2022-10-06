package com.innovation.backend.domain.DailyMission.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Mission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String mission;

  public Mission(String mission){
    this.mission = mission;
  }
}
