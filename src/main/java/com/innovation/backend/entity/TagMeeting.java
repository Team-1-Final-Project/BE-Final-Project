package com.innovation.backend.entity;

import javax.persistence.Column;
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
public class TagMeeting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;


//  @OneToMany(fetch = FetchType.LAZY, mappedBy = "meeting_tag", cascade = CascadeType.ALL)
//  @JsonIgnore
//  private List<Meeting> meetings;

}
