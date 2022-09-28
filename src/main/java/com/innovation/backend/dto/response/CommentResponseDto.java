package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Comment;

public class CommentResponseDto {
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
