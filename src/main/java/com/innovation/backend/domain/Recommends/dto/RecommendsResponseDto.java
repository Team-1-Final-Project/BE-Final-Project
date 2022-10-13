package com.innovation.backend.domain.Recommends.dto;
import com.innovation.backend.domain.Recommends.domain.Recommends;
import lombok.Getter;

@Getter
public class RecommendsResponseDto {
    private String title;
    private String address;
    private String content;
    private String image;

    public RecommendsResponseDto(Recommends recommends) {
        this.title = recommends.getTitle();
        this.address = recommends.getAddress();
        this.content = recommends.getContent();
        this.image = recommends.getImage();
    }
}
