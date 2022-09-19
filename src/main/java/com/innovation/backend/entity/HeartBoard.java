package com.innovation.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HeartBoard extends Timestamped{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

}
