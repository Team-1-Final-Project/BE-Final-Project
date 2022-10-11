package com.innovation.backend.domain.Board.dto.response;

import com.innovation.backend.domain.Board.domain.TagBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TagBoardResponseDto {
    private final Long id;
    private final String tagName;

    public TagBoardResponseDto(TagBoard tagBoard){
        this.id = tagBoard.getId();
        this.tagName = tagBoard.getTagName();
    }
}
