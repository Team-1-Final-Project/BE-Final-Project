package com.innovation.backend.exception;

import com.innovation.backend.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ResponseDto<?>> customExceptionHandler(CustomErrorException exception) {
    log.error(exception.getMessage());
    return new ResponseEntity<>(ResponseDto.fail(exception.getErrorCode()), HttpStatus.BAD_REQUEST);
  }

}
