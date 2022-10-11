package com.innovation.backend.domain.Chat.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageResponseDto {
    public enum MessageType {
        ENTER,
        TALK
    }
}
