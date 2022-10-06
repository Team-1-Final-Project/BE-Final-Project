package com.innovation.backend.service;


import com.innovation.backend.dto.response.RecommendsResponseDto;
import com.innovation.backend.entity.Recommends;
import com.innovation.backend.repository.RecommendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RecommendsService {

    private final RecommendsRepository recommendsRepository;

    public List<RecommendsResponseDto> getOfflineList(Pageable pageable) {

        Page<Recommends> recommendsList = recommendsRepository.findAll(pageable);
        List<RecommendsResponseDto> recommendsResponseDtoList = new ArrayList<>();

        for (Recommends recommends : recommendsList) {
            RecommendsResponseDto recommendsResponseDto = new RecommendsResponseDto(recommends);
            recommendsResponseDtoList.add(recommendsResponseDto);
        }
        return recommendsResponseDtoList;
    }
}


