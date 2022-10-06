package com.innovation.backend.chat.repository;

import com.innovation.backend.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository <ChatRoom, Long> {
    ChatRoom findChatRoomByMeetingId (ChatRoom chatRoom);
}
