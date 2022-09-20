package com.innovation.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting extends Timestamped{

  //모임 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //모임 이름
  @Column(nullable = false)
  private String title;

  //모임 내용
  @Column(nullable = false)
  private String content;

  //모임 모집 시작일
  @Column(nullable = false)
  private LocalDate startDate;

  //모임 모집 종료일
  @Column(nullable = false)
  private LocalDate endDate;

  //모임 시작일
  @Column(nullable = false)
  private LocalDateTime meetingDate;

  //모임 종료일
  @Column(nullable = false)
  private LocalDateTime meetingEndDate;

  //모임 지역?
  @Column(nullable = false)
  @JsonIgnore
  private String location;

  //모임 타입 (온라인/오프라인)
  @Column(nullable = false)
  private boolean online;

  //모임 최대 인원수
  @Column(nullable = false)
  private int limitPeople;

  //모임 현재 인원수
  @Column(nullable = false)
  private int nowPeople = 1;

  //모임 태그
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "TAG_NAME",nullable = false)
  private TagMeeting tag;

  //모임장 정보=>admin
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "ADMIN_ID", nullable = false)
  private Member admin;

  //참여자 정보->crew
  @OneToMany(mappedBy = "meetingJoin", orphanRemoval = true)
  @JsonIgnore
  private List<Crew> Crew = new ArrayList<>();

  // 좋아요 정보
  @OneToMany(mappedBy = "meetingLike", orphanRemoval = true)
  @JsonIgnore
  private List<HeartMeeting> LikeMeeting = new ArrayList<>();

  //좋아요 갯수
  private Integer totalLikeCount;


}
