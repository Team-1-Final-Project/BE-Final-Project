package com.innovation.backend.dto.response;

import com.innovation.backend.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private final Long id;
    private final String nickname;
    private final Authority authority;
}
