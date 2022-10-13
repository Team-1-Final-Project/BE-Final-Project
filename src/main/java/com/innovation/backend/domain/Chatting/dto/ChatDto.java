package com.innovation.backend.domain.Chatting.dto;

import com.innovation.backend.domain.Chatting.domain.Chat;
import com.innovation.backend.global.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto extends Timestamped {
    public enum ChatType {
        ENTER,
        TALK
    }

    private ChatType type;
    private String sender;
    private String nickname;
    private String profileImage;
    private String message;

    public ChatDto(Chat chat){
        this.sender = chat.getSender();
        this.nickname = chat.getNickname();
        this.profileImage = chat.getProfileImage();
        this.message = chat.getMessage();
        super.createdAt = chat.getCreatedAt();
    }
}
