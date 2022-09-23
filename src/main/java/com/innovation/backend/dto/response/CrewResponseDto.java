package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Member;
import lombok.Getter;

@Getter

public class CrewResponseDto {

  private Long memberId;
  private String nickname;

  public CrewResponseDto(Member member) {
    this.memberId = member.getId();
    this.nickname = member.getNickname();
  }

}
