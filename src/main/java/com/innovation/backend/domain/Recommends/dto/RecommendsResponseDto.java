package com.innovation.backend.domain.Recommends.dto;
import com.innovation.backend.domain.Recommends.domain.Recommends;
import lombok.Getter;

@Getter
public class RecommendsResponseDto {
    private final String title;
    private final String address;
    private final String content;
    private final String image;

    public RecommendsResponseDto(Recommends recommends) {
        this.title = recommends.getTitle();
        this.address = recommends.getAddress();
        this.content = recommends.getContent();
        this.image = recommends.getImage();
    }
}
