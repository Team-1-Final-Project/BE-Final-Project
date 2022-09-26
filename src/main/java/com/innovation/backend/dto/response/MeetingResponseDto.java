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
  private final Long meetingId;
  private final String title;
  private final String content;
  private final LocalDate joinStartDate;
  private final LocalDate joinEndDate;
  private final LocalDateTime meetingStartDate;
  private final LocalDateTime meetingEndDate;
  private final String location;
  private final int limitPeople;
  private final int nowPeople;
  private final LocalDateTime createdAt;
  private final MemberResponseDto admin;

 //  private TagMeeting tag;
  // private  Integer heartNums;

  //리스트 모임에 가입한 유저 목록
  private final List<CrewResponseDto> crews = new ArrayList<>();

  public MeetingResponseDto (Meeting meeting){
    this.meetingId = meeting.getId();
    this.title = meeting.getTitle();
    this.content = meeting.getContent();
    this.joinStartDate = meeting.getJoinStartDate();
    this.joinEndDate = meeting.getJoinEndDate();
    this.meetingStartDate = meeting.getMeetingStartDate();
    this.meetingEndDate = meeting.getMeetingEndDate();
    this.location = meeting.getLocation();
    this.limitPeople = meeting.getLimitPeople();
    this.nowPeople = meeting.getNowPeople();
//    this.tag = meeting.getTag();
    this.admin = new MemberResponseDto(meeting.getAdmin()); //모임장
    this.createdAt = meeting.getCreatedAt();
    for(Crew crew : meeting.getCrews()){
      CrewResponseDto crewResponseDto = new CrewResponseDto(crew);
      crews.add(crewResponseDto);
    }

  }

}
