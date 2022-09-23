package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.TagMeeting;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter

public class MeetingResponseDto {
  //모임 정보
  private  Long meetingId;
  private String title;
  private  String content;
  private  LocalDate startDate;
  private  LocalDate endDate;
  private  LocalDateTime meetingDate;
  private  LocalDateTime meetingEndDate;
  private  String location;
  private  int limitPeople;
  private  int nowPeople;
 // private  TagMeeting tag;
//  private  Integer heartNums;
  private  LocalDateTime createdAt;

  //모임장 정보
//  private  Long adminId;
//  private  String adminNickname;
//  private  String adminProfileImage;

  private MemberResponseDto admin;
//  private final boolean joinCheck;

  //리스트 모임에 가입한 유저 목록 (프로필이미지, 이름,보유 뱃지? )
  private List<Crew> crews = new ArrayList<>();

  public MeetingResponseDto (Meeting meeting){
    this.meetingId = meeting.getId();
    this.title = meeting.getTitle();
    this.content = meeting.getContent();
    this.startDate = meeting.getStartDate();
    this.endDate = meeting.getEndDate();
    this.meetingDate = meeting.getMeetingDate();
    this.meetingEndDate = meeting.getMeetingEndDate();
    this.location = meeting.getLocation();
    this.limitPeople = meeting.getLimitPeople();
    this.nowPeople = meeting.getNowPeople();
   // this.tag = meeting.getTag();
    this.admin = new MemberResponseDto(meeting.getAdmin());
    this.createdAt = meeting.getCreatedAt();

  }

}
