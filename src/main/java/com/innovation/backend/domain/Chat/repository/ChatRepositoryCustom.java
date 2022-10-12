package com.innovation.backend.domain.Chat.repository;

import com.innovation.backend.domain.Chat.entity.Message;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatRepositoryCustom {

    List<Message> getChatMessage(Long id);
}
