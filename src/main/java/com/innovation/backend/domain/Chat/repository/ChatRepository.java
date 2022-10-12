package com.innovation.backend.domain.Chat.repository;

import com.innovation.backend.domain.Chat.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Message, String>, ChatRepositoryCustom {
    Slice<Message> findAllByMeetingIdAndIdLessThanOrderByIdDesc(Pageable pageable);
}
