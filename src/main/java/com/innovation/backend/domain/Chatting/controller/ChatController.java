package com.innovation.backend.domain.Chatting.controller;

import com.innovation.backend.domain.Chatting.dto.ChatDto;
import com.innovation.backend.domain.Chatting.service.ChatService;
import com.innovation.backend.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

//    private final SimpMessageSendingOperations messageForm;
    private final ChatService chatService;

    @GetMapping("/meeting/{meetingId}/chatroom")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Slice<ChatDto>> chatLoad(@PathVariable("meetingId") Long meetingId, Pageable pageable) {
        return chatService.chatLoad(meetingId, pageable);
    }

//    @MessageMapping("/chat/message")
//    public void meetingChat(ChatDto chatDto) {
//
//        chatService.saveChat(chatDto);
//        messageForm.convertAndSend(
//                "/sub/chat/" + chatDto);
//    }
}
