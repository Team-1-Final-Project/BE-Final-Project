package com.innovation.backend.domain.Crew.dto;

import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Member.dto.response.BadgeResponseDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CrewDetailResponseDto {
    private final Long memberId;
    private final Long meetingId;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final List<BadgeResponseDto> badgeList = new ArrayList<>();

    public CrewDetailResponseDto(Crew crew) {

        this.memberId = crew.getMember().getId();
        this.meetingId = crew.getMeeting().getId();
        this.email = crew.getMember().getEmail();
        this.nickname = crew.getMember().getNickname();
        this.profileImage = crew.getMember().getProfileImage();

        for (Badge badge : crew.getMember().getBadgeList()) {
            BadgeResponseDto badgeResponseDto = new BadgeResponseDto(badge.getTagBadge());
            badgeList.add(badgeResponseDto);
        }
    }
}

