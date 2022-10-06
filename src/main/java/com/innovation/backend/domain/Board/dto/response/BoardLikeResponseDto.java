package com.innovation.backend.domain.Board.dto.response;

import lombok.Getter;

@Getter
public class BoardLikeResponseDto {
    private boolean isBoardLike;
    public BoardLikeResponseDto(boolean boardLike) {
        this.isBoardLike = boardLike;
    }
}
