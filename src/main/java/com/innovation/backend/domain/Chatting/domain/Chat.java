package com.innovation.backend.domain.Chatting.domain;

import com.innovation.backend.domain.Chatting.dto.ChatDto;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.global.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Component
public class Chat extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String sender;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profileImage;

    @Column(columnDefinition = "LONGTEXT")
    private String message;

    @JoinColumn(name ="meeting_id",nullable = false)
    @ManyToOne
    private Meeting meeting;


    public Chat(ChatDto chatDto) {
        this.sender = chatDto.getSender();
        this.nickname = chatDto.getNickname();
        this.profileImage = chatDto.getProfileImage();
        this.message = chatDto.getMessage();
    }


}
