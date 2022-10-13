package com.innovation.backend.domain.Badge.domain;


import com.innovation.backend.domain.Member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagBadge_id")
    private TagBadge tagBadge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Badge(TagBadge tagBadge, Member member){
        this.tagBadge = tagBadge;
        this.member = member;
    }

}