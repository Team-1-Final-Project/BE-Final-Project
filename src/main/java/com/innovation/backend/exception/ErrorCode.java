package com.innovation.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

  ENTITY_NOT_FOUND("ENTITY_NOT_FOUND","데이터가 존재하지 않습니다."),
  INVALID_ERROR("INVALID_ERROR","에러 발생"),

  NEED_LOGIN("NEED_LOGIN","로그인이 필요합니다."),
  NOT_ADMIN_OF_MEETING("NOT_ADMIN_OF_MEETING","해당 모임 관리자가 아닙니다."),
  NOT_FOUND_MEETING("NOT_FOUND_MEETING","모임을 찾을 수 없습니다.");

  private final String code;
  private final String message;

}

