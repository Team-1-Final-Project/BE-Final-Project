package com.innovation.backend.chat.repository;

import com.innovation.backend.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, String> {
}
