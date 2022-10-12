package com.innovation.backend.domain.Badge.repository;


import com.innovation.backend.domain.Badge.domain.TagBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagBadgeRepository extends JpaRepository<TagBadge, Long> {
    TagBadge findByBadgeName(String badgeName);
}
