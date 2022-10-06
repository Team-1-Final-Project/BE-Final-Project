package com.innovation.backend.domain.Recommends.dto;
import com.innovation.backend.domain.Recommends.domain.Recommends;
import lombok.Getter;

@Getter
public class RecommendsResponseDto {
    private String title;
    private String address;

    public RecommendsResponseDto(Recommends recommends) {
        this.title = recommends.getTitle();
        this.address = recommends.getAddress();
    }
}
