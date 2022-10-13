package com.innovation.backend.domain.Board.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class TagBoardRequestDto {
    private List<Long> tagIds;
}
