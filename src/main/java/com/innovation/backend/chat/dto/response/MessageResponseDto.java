package com.innovation.backend.chat.dto.response;

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
