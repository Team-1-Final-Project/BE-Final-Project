package com.innovation.backend.domain.Badge;


import com.innovation.backend.domain.Badge.TagBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagBadgeRepository extends JpaRepository<TagBadge, Long> {
    TagBadge findByBadgeName(String badgeName);
}
