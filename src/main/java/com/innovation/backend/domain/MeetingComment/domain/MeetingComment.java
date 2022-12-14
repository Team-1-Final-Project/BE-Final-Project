package com.innovation.backend.domain.MeetingComment.domain;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.MeetingComment.dto.request.MeetingCommentRequestDto;
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

public class MeetingComment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name = "meeting_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Meeting meeting;


    public MeetingComment(Meeting meeting, MeetingCommentRequestDto meetingCommentRequestDto, Member member) {
        this.meeting = meeting;
        this.content = meetingCommentRequestDto.getContent();
        this.member = member;
    }

    public void alter(MeetingCommentRequestDto meetingCommentRequestDto) {
        this.content = meetingCommentRequestDto.getContent();
    }

    public boolean validateMember(Long memberId) {
        Long thisMemberId = this.member.getId();
        return !memberId.equals(thisMemberId);
    }


}
