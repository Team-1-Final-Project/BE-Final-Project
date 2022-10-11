package com.innovation.backend.domain.Chat.dto.request;

import com.innovation.backend.domain.Chat.entity.ChatRoom;
import com.innovation.backend.entity.Crew;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
