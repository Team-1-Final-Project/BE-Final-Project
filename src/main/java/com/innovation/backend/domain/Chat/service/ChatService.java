package com.innovation.backend.domain.Chat.service;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Chat.dto.MessageDto;
import com.innovation.backend.domain.Chat.entity.Message;
import com.innovation.backend.domain.Chat.repository.ChatRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public ResponseDto<Slice<MessageDto>> chatLoad(Long meetingId, Pageable pageable) {

        Slice<Message> MessageList = chatRepository.findAllByMeetingIdAndIdLessThanOrderByIdDesc(pageable); {
            PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return
    }
}
