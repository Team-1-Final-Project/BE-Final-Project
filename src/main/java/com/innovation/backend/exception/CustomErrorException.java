package com.innovation.backend.exception;

public class CustomErrorException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomErrorException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}