package com.innovation.backend.domain.Chatting.service;

import com.innovation.backend.domain.Chatting.domain.Chat;
import com.innovation.backend.domain.Chatting.dto.ChatDto;
import com.innovation.backend.domain.Chatting.repository.ChatRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Alert;
import org.springframework.data.domain.*;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.message.MessageInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Chat chat;
    private final ChatRepository chatRepository;
    private final MeetingRepository meetingRepository;

    public ResponseDto<Slice<ChatDto>> chatLoad(Long meetingId, Pageable pageable) {

        Meeting meeting = isPresentMeeting(meetingId);

        if(null == meeting) {
            return ResponseDto.fail(ErrorCode.NOT_FOUND_MEETING);
        }

        Slice<Chat> chatting = chatRepository.findAllByOrderByIdDesc(pageable); {
            PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "id"));
        }
        List<ChatDto> chatDtoList = new ArrayList<>(chatRepository.findAllByOrderByIdDesc());

        return ResponseDto.success(new PageImpl<>(chatDtoList, pageable, chatting.getNumberOfElements()));
    }

    @Transactional(readOnly = true)
    public Meeting isPresentMeeting(Long meetingId) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        return optionalMeeting.orElse(null);
    }

    @Transactional
    public void saveChat(ChatDto chatDto) {

        chatRepository.save(new Chat(chatDto));
    }
}
