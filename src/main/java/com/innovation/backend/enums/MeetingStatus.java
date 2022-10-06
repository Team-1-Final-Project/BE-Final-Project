package com.innovation.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MeetingStatus {
  READY_FOR_JOIN("READY_FOR_JOIN","모집 준비 중"),
  CAN_JOIN("CAN_JOIN","모집 중"),
  COMPLETE_JOIN("COMPLETE_JOIN","모집 완료"),
  PASS_DEADLINE("PASS_DEADLINE","모집 기한이 지난 모임"),
  COMPLETED_MEETING("COMPLETED_MEETING","모임 완료");

  private final String code;
  private final String message;

  MeetingStatus(String code, String message){
    this.code = code;
    this.message = message;
  }

}
