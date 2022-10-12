package com.innovation.backend.domain.Meeting.dto.response;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Crew.dto.CrewResponseDto;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.domain.MeetingTagConnection;
import com.innovation.backend.domain.Meeting.domain.TagMeeting;
import com.innovation.backend.domain.Member.dto.response.MemberResponseDto;
import com.innovation.backend.global.enums.MeetingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class MeetingGetAllResponseDto {

  //모임 정보
  private final Long id;
  private final String title;
  private final String content;
  private final LocalDate joinStartDate;
  private final LocalDate joinEndDate;
  private final LocalDate meetingStartDate;
  private final LocalDate meetingEndDate;
  private final String location;
  private final String meetingImage;
  private final String meetingThumbImage;
  private final int limitPeople;
  private final int nowPeople;
  private final LocalDateTime createdAt;
  private final MemberResponseDto admin;
  private final Long heartNums;
  private final MeetingStatus meetingStatus;
  private final List<TagMeetingResponseDto> tagMeetings = new ArrayList<>();

  //모임에 가입한 유저 목록
  private final List<CrewResponseDto> crews = new ArrayList<>();

  public MeetingGetAllResponseDto(Meeting meeting) {
    this.id = meeting.getId();
    this.title = meeting.getTitle();
    this.content = meeting.getContent();
    this.joinStartDate = meeting.getJoinStartDate();
    this.joinEndDate = meeting.getJoinEndDate();
    this.meetingStartDate = meeting.getMeetingStartDate();
    this.meetingEndDate = meeting.getMeetingEndDate();
    this.location = meeting.getLocation();
    this.meetingImage = meeting.getMeetingImage();
    this.meetingThumbImage = meeting.getMeetingThumbImage();
    this.limitPeople = meeting.getLimitPeople();
    this.nowPeople = meeting.getNowPeople();
    this.meetingStatus = meeting.getMeetingStatus();
    this.heartNums = meeting.getHeartNums();
    this.admin = new MemberResponseDto(meeting.getAdmin()); //모임장

    this.createdAt = meeting.getCreatedAt();

    for (Crew crew : meeting.getCrews()) {
      CrewResponseDto crewResponseDto = new CrewResponseDto(crew);
      crews.add(crewResponseDto);
    }

    for(MeetingTagConnection meetingTagConnection : meeting.getMeetingTagConnectionList()){
      TagMeeting tagMeeting = meetingTagConnection.getTagMeeting();
      TagMeetingResponseDto tagMeetingResponseDto = new TagMeetingResponseDto(tagMeeting);
      tagMeetings.add(tagMeetingResponseDto);
    }
  }
}
