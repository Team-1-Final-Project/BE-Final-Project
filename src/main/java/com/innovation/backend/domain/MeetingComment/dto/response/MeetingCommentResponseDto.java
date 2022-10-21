package com.innovation.backend.domain.MeetingComment.dto.response;

import com.innovation.backend.domain.MeetingComment.domain.MeetingComment;
import com.innovation.backend.global.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MeetingCommentResponseDto extends Timestamped {
    private Long commentId;
    private String nickname;
    private String profileImage;
    private String content;

    public MeetingCommentResponseDto(MeetingComment meetingComment){
        this.commentId = meetingComment.getId();
        this.nickname = meetingComment.getMember().getNickname();
        this.profileImage = meetingComment.getMember().getProfileImage();
        this.content = meetingComment.getContent();
        super.createdAt = meetingComment.getCreatedAt();
        super.modifiedAt = meetingComment.getModifiedAt();
    }
}
