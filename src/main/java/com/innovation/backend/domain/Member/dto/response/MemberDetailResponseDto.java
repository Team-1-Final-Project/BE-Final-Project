package com.innovation.backend.domain.Member.dto.response;

import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberDetailResponseDto {
    private Long id;
    private String nickname;
    private String email;
    private Authority authority;
    private String profileImage;
    private final List<BadgeResponseDto> badgeList = new ArrayList<>();

    public MemberDetailResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.authority = member.getAuthority();
        this.profileImage = member.getProfileImage();

        for (Badge badge : member.getBadgeList()) {
            BadgeResponseDto badgeResponseDto = new BadgeResponseDto(badge.getTagBadge());
            badgeList.add(badgeResponseDto);
        }
    }
}
