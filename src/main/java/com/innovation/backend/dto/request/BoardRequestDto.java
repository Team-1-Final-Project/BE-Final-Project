package com.innovation.backend.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    private String title;
    private String content;
    private List<String> tagBoard;

}
