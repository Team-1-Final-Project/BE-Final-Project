package com.innovation.backend.dto.response;

import lombok.Getter;

@Getter
public class MeetingLikeResponseDto {
    private boolean isMeetingLike;

    public MeetingLikeResponseDto(boolean MeetingLike) {
        this.isMeetingLike = MeetingLike;
    }
}
