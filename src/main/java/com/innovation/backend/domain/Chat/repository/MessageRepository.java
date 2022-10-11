package com.innovation.backend.domain.Chat.repository;

import com.innovation.backend.domain.Chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
