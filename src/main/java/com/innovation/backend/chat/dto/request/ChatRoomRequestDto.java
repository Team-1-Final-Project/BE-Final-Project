package com.innovation.backend.chat.dto.request;

import com.innovation.backend.chat.entity.ChatRoom;
import com.innovation.backend.entity.Crew;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoomRequestDto {

    private Crew sender;
    private Crew receiver;

    public void setUserInfo(ChatRoom chatRoom){
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
    }

    public ChatRoomRequestDto(ChatRoom chatRoom){
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
    }

}
