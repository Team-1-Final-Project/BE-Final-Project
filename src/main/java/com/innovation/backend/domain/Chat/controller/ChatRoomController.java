package com.innovation.backend.domain.Chat.controller;


import com.innovation.backend.domain.Chat.dto.response.ChatRoomResponseDto;
import com.innovation.backend.domain.Chat.service.ChatRoomService;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/meeting/{meetingId}/chatroom")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<ChatRoomResponseDto> joinChat(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        return chatRoomService.joinChat(userDetails, id);
    }


}
