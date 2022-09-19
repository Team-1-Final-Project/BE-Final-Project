package com.innovation.backend.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

  ENTITY_NOT_FOUND("ENTITY_NOT_FOUND","데이터가 존재하지 않습니다."),
  INVALID_ERROR("INVALID_ERROR","에러 발생");

  private final String code;
  private final String message;

}

