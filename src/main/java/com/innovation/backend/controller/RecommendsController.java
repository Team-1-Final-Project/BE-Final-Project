package com.innovation.backend.controller;

import com.innovation.backend.dto.response.RecommendsResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.RecommendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendsController {

    private final RecommendsService recommendsService;

    @GetMapping("/zeroshop/offline")
    public ResponseDto<List<RecommendsResponseDto>> getOfflineList(@PageableDefault(page =0 ,sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        List<RecommendsResponseDto> recommendsResponseDto;
        recommendsResponseDto = recommendsService.getOfflineList(pageable);
        return ResponseDto.success(recommendsResponseDto);
    }
}
