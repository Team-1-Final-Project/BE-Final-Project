package com.innovation.backend.global.common.response;

import lombok.Getter;

@Getter
public class LikeResultResponseDto {
    private String message;
    public LikeResultResponseDto(String message) {
        this.message = message;
    }
}
