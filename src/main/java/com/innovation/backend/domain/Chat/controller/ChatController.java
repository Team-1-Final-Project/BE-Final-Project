package com.innovation.backend.domain.Chat.controller;


import com.innovation.backend.domain.Chat.dto.MessageDto;
import com.innovation.backend.domain.Chat.service.ChatService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messageForm;
    private final ChatService chatService;

    @GetMapping("/meeting/{meetingId}/chatroom")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Slice<MessageDto>> chatLoad(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long meetingId, Pageable pageable) {
        return chatService.chatLoad(userDetails, meetingId, pageable);
    }


    @MessageMapping("/chat/message")
    public void meetingChat(MessageDto messageDto) {
        if(MessageDto.MessageType.ENTER.equals(messageDto.getType())){
            messageDto.setContent(messageDto.getSender() + "님이 입장하셨습나다.");
        }
        chatService.saveChat(messageDto);
        messageForm.convertAndSend(
                "/sub/chat/" + messageDto);
    }
}
