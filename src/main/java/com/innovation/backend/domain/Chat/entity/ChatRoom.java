package com.innovation.backend.domain.Chat.entity;

import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.domain.Crew.domain.Crew;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private Crew sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private Crew receiver;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messageList;

    @Builder
    public ChatRoom(Crew sender, Crew receiver){
        this.sender = sender;
        this.receiver = receiver;
    }

}
