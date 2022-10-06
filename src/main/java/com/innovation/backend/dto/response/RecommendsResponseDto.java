package com.innovation.backend.dto.response;
import com.innovation.backend.entity.Recommends;
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
