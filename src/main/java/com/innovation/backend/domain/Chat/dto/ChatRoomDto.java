package com.innovation.backend.domain.Chat.dto;

import com.innovation.backend.domain.Chat.entity.ChatRoom;
import com.innovation.backend.domain.Chat.entity.Message;
import com.innovation.backend.domain.Crew.domain.Crew;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ChatRoomDto {
    private Crew sender;
    private Crew receiver;
    private List<MessageDto> messageDtoList;

    public void participant(ChatRoom chatRoom){
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
    }

    public ChatRoomDto(ChatRoom chatRoom, List<MessageDto> messageDtoList){
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
        this.messageDtoList = messageDtoList;
    }
}
