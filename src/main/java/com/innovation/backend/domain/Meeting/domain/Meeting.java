package com.innovation.backend.domain.Meeting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.innovation.backend.domain.Crew.domain.Crew;
//import com.innovation.backend.enums.MeetingStatus;
import com.innovation.backend.domain.Meeting.dto.request.MeetingRequestDto;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Review.domain.Review;
import com.innovation.backend.global.enums.MeetingStatus;
import com.innovation.backend.global.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting extends Timestamped {

  //모임 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //모임 이름
  @Column(nullable = false)
  private String title;

  //모임 내용
  @Column(nullable = false,columnDefinition = "TEXT")
  private String content;

  //모임 이미지
  @Column
  private String meetingImage;

  private String meetingThumbImage;

  //모임 모집 시작일
  @Column(nullable = false)
  private LocalDate joinStartDate;

  //모임 모집 종료일
  @Column(nullable = false)
  private LocalDate joinEndDate;

  //모임 시작일
  @Column(nullable = false)
  private LocalDate meetingStartDate;

  //모임 종료일
  @Column(nullable = false)
  private LocalDate meetingEndDate;

  //모임 지역
  @Column(nullable = false)
  @JsonIgnore
  private String location;


  //모임 최대 인원수
  @Column(nullable = false)
  private int limitPeople;

  //모임 현재 인원수
  @Column(nullable = false)
  private int nowPeople = 1;

  //모임 상태
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MeetingStatus meetingStatus;

  //모임 좋아요수
  private Long heartNums = Long.valueOf(0);

  //모임 태그
  @OneToMany(mappedBy = "meeting",cascade = CascadeType.REMOVE,orphanRemoval = true)
  @JsonIgnore
  private Set<MeetingTagConnection> meetingTagConnectionList = new HashSet<>();


  //모임장 정보=>admin
  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "ADMIN_ID", nullable = false)
  private Member admin;

  //참여자 정보->crew
  @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE,orphanRemoval = true)
  @JsonIgnore
  private List<Crew> crews = new ArrayList<>();

  // 좋아요 정보
  @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
  private Set<HeartMeeting> heartMeetings = new HashSet<>();

  //후기
  @OneToMany(mappedBy = "meeting", cascade = CascadeType.REMOVE,orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  //모임 생성
  public Meeting(MeetingRequestDto requestDto, Member member, String meetingImage, String meetingThumbImage){
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.meetingImage = meetingImage;
    this.meetingThumbImage = meetingThumbImage;
    this.joinStartDate = requestDto.getJoinStartDate();
    this.joinEndDate = requestDto.getJoinEndDate();
    this.meetingStartDate = requestDto.getMeetingStartDate();
    this.meetingEndDate = requestDto.getMeetingEndDate();
    this.location = requestDto.getLocation();
    this.limitPeople = requestDto.getLimitPeople();
    if(requestDto.getJoinStartDate().equals(LocalDate.now()) || requestDto.getJoinStartDate().isBefore(LocalDate.now())){
      this.meetingStatus = MeetingStatus.CAN_JOIN;
    }else if(requestDto.getJoinStartDate().isAfter(LocalDate.now())){
      this.meetingStatus = MeetingStatus.READY_FOR_JOIN;
    }
    this.admin = member;
  }

  public void setMeetingTagConnectionList (Set<MeetingTagConnection> meetingTagConnections){
    this.meetingTagConnectionList = meetingTagConnections;
  }

  //모임장 확인
  public boolean isWrittenBy(Member member) {
    return this.admin.getId().equals(member.getId());
  }

  //모임 수정
  public void update(MeetingRequestDto requestDto,String meetingImage, String meetingThumbImage){
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.meetingImage = requestDto.getMeetingImage();
    this.meetingThumbImage = meetingThumbImage;
    this.joinStartDate = requestDto.getJoinStartDate();
    this.joinEndDate = requestDto.getJoinEndDate();
    this.meetingStartDate = requestDto.getMeetingStartDate();
    this.meetingEndDate = requestDto.getMeetingEndDate();
    this.location = requestDto.getLocation();
    this.limitPeople = requestDto.getLimitPeople();
    this.meetingImage = meetingImage;
    if(this.nowPeople < this.limitPeople){
      this.meetingStatus = MeetingStatus.CAN_JOIN;
    }else {
      this.meetingStatus = MeetingStatus.COMPLETE_JOIN;
    }
  }

  //참가자 추가
  public void addNowPeople(){
    this.nowPeople += 1;
  }

  //참가자 빼기
  public void minusNowPeople(){
    this.nowPeople -= 1;
  }

  // 크루 추가
  public void addCrew(Crew crew) {
    this.crews.add(crew);
  }
  // 크루 빼기
  public void deleteCrew(Crew crew) {
    this.crews.remove(crew);
  }

  //좋아요수 증가
  public void addMeetingLike(Long heartNums){
    this.heartNums = heartNums;
  }

  //모임상태 변경
  public void setMeetingStatus (MeetingStatus meetingStatus){
    this.meetingStatus = meetingStatus;
  }
}




