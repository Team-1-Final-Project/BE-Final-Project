package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Comment;
import com.innovation.backend.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class CommentResponseDto extends Timestamped {
    private Long commentId;
    private String commentWriter;
    private String profileImage;
    private String content;

    public CommentResponseDto(Comment comment){
        this.commentId = comment.getId();
        this.commentWriter = comment.getMember().getNickname();
        this.profileImage = comment.getMember().getProfileImage();
        this.content = comment.getContent();
    }

}
