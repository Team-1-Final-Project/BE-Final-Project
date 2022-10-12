package com.innovation.backend.domain.Chat.entity;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Chat.dto.MessageDto;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message extends Timestamped {
    private MessageDto.MessageType type;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String sender;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @JoinColumn(name ="meeting_id",nullable = false)
    @ManyToOne
    private Meeting meeting;


    public Message(MessageDto messageDto) {
        this.sender = messageDto.getSender();
        this.nickname = messageDto.getNickname();
        this.profileImage = messageDto.getProfileImage();
        this.content = messageDto.getContent();
    }


}
