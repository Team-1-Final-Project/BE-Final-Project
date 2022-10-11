package com.innovation.backend.domain.DailyMission.repository;

import com.innovation.backend.domain.DailyMission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findById(Long id);
    Mission findFirstByOrderByIdDesc();
    long count();

}
