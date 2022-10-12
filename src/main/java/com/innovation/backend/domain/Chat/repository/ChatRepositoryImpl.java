package com.innovation.backend.domain.Chat.repository;

import com.innovation.backend.domain.Chat.entity.Message;
import com.innovation.backend.domain.Chat.entity.QMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

import static com.innovation.backend.domain.Chat.entity.QMessage.message;
import static com.innovation.backend.domain.Meeting.domain.QMeeting.meeting;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Message> getChatMessage(Long id) {
//        QMessage message = QMessage.message;
        return jpaQueryFactory.selectFrom(message)
                .where(message.meeting.id.eq(meeting.id).and(message.id.lt(id)))
                .orderBy(message.id.desc())
                .fetch();
    }
}
