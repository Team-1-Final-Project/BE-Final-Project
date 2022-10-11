package com.innovation.backend.dto.response;

import com.innovation.backend.entity.TagBoard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
