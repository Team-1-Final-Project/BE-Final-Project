package com.innovation.backend.domain.Badge.repository;



import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Badge.domain.TagBadge;
import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByMember(Member member);

    boolean existsByMember(Member member);

    boolean existsByMemberAndTagBadge(Member member, TagBadge tagBadge);
}
