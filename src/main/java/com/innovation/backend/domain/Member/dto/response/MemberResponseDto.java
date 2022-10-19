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
public class MemberResponseDto {
    private  Long id;
    private  String nickname;
    private String email;
    private  Authority authority;
    private  String profileImage;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.authority = member.getAuthority();
        this.profileImage = member.getProfileImage();
    }

}
