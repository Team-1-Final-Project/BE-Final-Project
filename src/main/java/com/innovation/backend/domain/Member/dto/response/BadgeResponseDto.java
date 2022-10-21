package com.innovation.backend.domain.Member.dto.response;

import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Badge.domain.TagBadge;
import lombok.Getter;

@Getter
public class BadgeResponseDto {

    private Long id;
    private String badgeName;
    private boolean isSignature;

    public BadgeResponseDto(TagBadge tagBadge) {
        this.id = tagBadge.getId();
        this.badgeName = tagBadge.getBadgeName();
    }

    public BadgeResponseDto(TagBadge tagBadge, Badge badge) {
        this.id = tagBadge.getId();
        this.badgeName = tagBadge.getBadgeName();
        this.isSignature = badge.isSignature();
    }
}
