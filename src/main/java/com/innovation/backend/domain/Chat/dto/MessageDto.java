package com.innovation.backend.domain.Chat.dto;

import com.innovation.backend.domain.Chat.entity.Message;
import com.innovation.backend.global.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto extends Timestamped {
    public enum MessageType {
        ENTER,
        TALK
    }
    private MessageType type;
    private String sender;
    private String nickname;
    private String profileImage;
    private String content;

    public MessageDto(Message message){
        if(message.getType() !=MessageType.ENTER) this.type = MessageType.TALK;
        this.sender = message.getSender();
        this.nickname = message.getNickname();
        this.profileImage = message.getProfileImage();
        this.content = message.getContent();
        super.createdAt = message.getCreatedAt();
    }
}
