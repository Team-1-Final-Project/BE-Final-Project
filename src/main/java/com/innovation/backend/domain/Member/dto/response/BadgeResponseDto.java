package com.innovation.backend.domain.Member.dto.response;

import com.innovation.backend.domain.Badge.domain.TagBadge;
import lombok.Getter;

@Getter
public class BadgeResponseDto {

    private Long id;
    private String badgeName;
    public BadgeResponseDto(TagBadge tagBadge) {
        this.id = tagBadge.getId();
        this.badgeName = tagBadge.getBadgeName();
    }
}
