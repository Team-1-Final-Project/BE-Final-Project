package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private  Long id;
    private  String nickname;
    private  Authority authority;
    private  String profileImage;

    public MemberResponseDto(Member member){
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
    }

}
