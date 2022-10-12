package com.innovation.backend.domain.Chatting.repository;


import com.innovation.backend.domain.Chatting.domain.Chat;
import com.innovation.backend.domain.Chatting.dto.ChatDto;
import com.innovation.backend.domain.Meeting.domain.Meeting;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Slice<Chat> findAllByOrderByIdDesc(Pageable pageable);
    List<ChatDto> findAllByOrderByIdDesc();
}
