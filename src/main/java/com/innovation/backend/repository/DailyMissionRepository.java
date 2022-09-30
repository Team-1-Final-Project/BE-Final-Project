package com.innovation.backend.repository;

import com.innovation.backend.entity.DailyMission;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DailyMissionRepository extends JpaRepository<DailyMission, Long> {
    Optional<DailyMission> findById(Long id);
    DailyMission findFirstByOrderByIdDesc();
    boolean existsByMemberAndCreatedAtBetween(Member member,LocalDateTime start, LocalDateTime end);
    boolean existsByMissionAndCreatedAtBetween(String mission,LocalDateTime start, LocalDateTime end);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
;}
