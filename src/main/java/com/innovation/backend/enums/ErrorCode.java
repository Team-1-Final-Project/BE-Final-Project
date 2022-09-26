package com.innovation.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

  ENTITY_NOT_FOUND("ENTITY_NOT_FOUND","데이터가 존재하지 않습니다."),
  INVALID_ERROR("INVALID_ERROR","에러 발생"),


  //회원정보

  NEED_LOGIN("NEED_LOGIN","로그인이 필요합니다."),
  BAD_TOKEN_REQUEST("BAD_TOKEN_REQUEST", "Token이 유효하지 않습니다."),
  TOKEN_NOT_FOUND("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다."),

  DUPLICATED_EMAIL("DUPLICATED_EMAIL", "이미 가입된 회원입니다."),

  DUPLICATED_NICKNAME("DUPLICATED_NICKNAME","중복된 닉네임입니다."),

  USED_EMAIL("USED_EMAIL", "중복된 회원정보입니다."),
  EMAIL_NOT_FOUND("EMAIL_NOT_FOUND", "회원정보가 일치하지 않습니다."),

  MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "가입정보를 찾을 수 없습니다."),
  PASSWORDS_NOT_MATCHED("PASSWORDS_NOT_MATCHED", "회원정보가 일치하지 않습니다."),

  NOT_SAME_MEMBER("NOT_SAME_MEMBER", "작성자만 수정이 가능합니다."),
  //모임
  NOT_ADMIN_OF_MEETING("NOT_ADMIN_OF_MEETING","해당 모임 관리자가 아닙니다."),
  NOT_FOUND_MEETING("NOT_FOUND_MEETING","모임을 찾을 수 없습니다."),
  ALREADY_JOIN("ALREADY_JOIN","이미 참여한 모임입니다."),
  NEVER_JOIN("NEVER_JOIN","참여 신청하지 않은 모임입니다."),
  ADMIN_CANNOT_CANCEL_JOIN("ADMIN_CANNOT_CANCEL_JOIN", "모임장은 탈퇴할 수 없습니다.");

  private final String code;
  private final String message;

  ErrorCode(String code, String message){
    this.code = code;
    this.message = message;
  }

}

