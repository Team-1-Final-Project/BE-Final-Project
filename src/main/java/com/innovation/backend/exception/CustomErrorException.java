package com.innovation.backend.exception;

import com.innovation.backend.enums.ErrorCode;
import lombok.Getter;

@Getter
public class CustomErrorException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomErrorException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
