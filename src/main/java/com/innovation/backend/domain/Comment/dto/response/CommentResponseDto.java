package com.innovation.backend.domain.Comment.dto.response;

import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.global.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CommentResponseDto extends Timestamped {
    private Long commentId;
    private String email;
    private String commentWriter;
    private String profileImage;
    private String content;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.email = comment.getMember().getEmail();
        this.commentWriter = comment.getMember().getNickname();
        this.profileImage = comment.getMember().getProfileImage();
        this.content = comment.getContent();
        super.createdAt = comment.getCreatedAt();
    }

}
