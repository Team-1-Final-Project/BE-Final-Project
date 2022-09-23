package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.TagMeeting;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MeetingResponseDto {
  //모임 정보
  private final Long campaignId;
  private final String title;
  private final String content;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final LocalDateTime meetingDate;
  private final LocalDateTime meetingEndDate;
  private final String location;
  private final boolean online;
  private final int limitPeople;
  private final int nowPeople;
  private final TagMeeting tag;
  private final Integer heartNums;

  //모임장 정보
//  private final String adminName;
//  private final String adminProfileImage;
//  private final boolean joinCheck;


  //리스트 모임에 가입한 유저 목록 (프로필이미지, 이름,보유 뱃지? )
private List<Crew> crews;

}
