package com.innovation.backend.domain.Chat.repository;

import com.innovation.backend.domain.Chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository <ChatRoom, Long> {
    ChatRoom findChatRoomByMeetingId (ChatRoom chatRoom);
}
