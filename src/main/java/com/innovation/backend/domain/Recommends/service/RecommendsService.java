package com.innovation.backend.domain.Recommends.service;


import com.innovation.backend.domain.Recommends.domain.Recommends;
import com.innovation.backend.domain.Recommends.dto.RecommendsResponseDto;
import com.innovation.backend.domain.Recommends.repository.RecommendsRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecommendsService {

    private final RecommendsRepository recommendsRepository;

    public List<RecommendsResponseDto> getOffLineList(Pageable pageable) {

        Page<Recommends> recommendsList = recommendsRepository.findAllByType(pageable, "OffLine");
        return CommonResponse(recommendsList);
    }

    public List<RecommendsResponseDto> getOnLineList(Pageable pageable) {

        Page<Recommends> recommendsList = recommendsRepository.findAllByType(pageable, "OnLine");
        return CommonResponse(recommendsList);
    }

    private List<RecommendsResponseDto> CommonResponse(Page<Recommends> recommendsList) {
        List<RecommendsResponseDto> recommendsResponseDtoList = new ArrayList<>();

        for (Recommends recommends : recommendsList) {
            RecommendsResponseDto recommendsResponseDto = new RecommendsResponseDto(recommends);
            recommendsResponseDtoList.add(recommendsResponseDto);
        }
        return recommendsResponseDtoList;
    }
}


