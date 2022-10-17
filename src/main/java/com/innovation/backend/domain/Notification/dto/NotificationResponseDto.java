package com.innovation.backend.domain.Notification.dto;

import lombok.Getter;

@Getter
public class NotificationResponseDto {
    Long id;
    String content;

    public NotificationResponseDto(Long id, String content){
        this.id = id;
        this.content = content;
    }

}
