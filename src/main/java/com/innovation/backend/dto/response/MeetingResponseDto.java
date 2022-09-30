package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.MeetingTagConnection;
import com.innovation.backend.entity.TagMeeting;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.enums.MeetingStatus;
import com.innovation.backend.repository.TagMeetingRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter

public class MeetingResponseDto {

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
  private final int limitPeople;
  private final int nowPeople;
  private final LocalDateTime createdAt;
  private final MemberResponseDto admin;
  private final Long heartNums;
  private final MeetingStatus meetingStatus;
  private final List<TagMeetingResponseDto> tagMeetings = new ArrayList<>();
  // private  Integer heartNums;

  //모임에 가입한 유저 목록
  private final List<CrewResponseDto> crews = new ArrayList<>();

  public MeetingResponseDto(Meeting meeting) {
    this.id = meeting.getId();
    this.title = meeting.getTitle();
    this.content = meeting.getContent();
    this.joinStartDate = meeting.getJoinStartDate();
    this.joinEndDate = meeting.getJoinEndDate();
    this.meetingStartDate = meeting.getMeetingStartDate();
    this.meetingEndDate = meeting.getMeetingEndDate();
    this.location = meeting.getLocation();
    this.meetingImage = meeting.getMeetingImage();
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
