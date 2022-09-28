package com.innovation.backend.dto.request;

import com.innovation.backend.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Long boardId;
    private String content;
}
