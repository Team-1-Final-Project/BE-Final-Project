package com.innovation.backend.domain.Badge;



import com.innovation.backend.domain.Badge.Badge;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Badge.TagBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByMember(Member member);

    boolean existsByMember(Member member);

    boolean existsByMemberAndTagBadge(Member member, TagBadge tagBadge);
}
