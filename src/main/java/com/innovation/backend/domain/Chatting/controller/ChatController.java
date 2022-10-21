package com.innovation.backend.domain.Chatting.controller;

//@RestController
//@RequiredArgsConstructor
//public class ChatController {
//
////    private final SimpMessageSendingOperations messageForm;
//    private final ChatService chatService;
//
//    @GetMapping("/meeting/{meetingId}/chatroom")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseDto<Slice<ChatDto>> chatLoad(@PathVariable("meetingId") Long meetingId, Pageable pageable) {
//        return chatService.chatLoad(meetingId, pageable);
//    }

//    @MessageMapping("/chat/message")
//    public void meetingChat(ChatDto chatDto) {
//
//        chatService.saveChat(chatDto);
//        messageForm.convertAndSend(
//                "/sub/chat/" + chatDto);
//    }
//}
