package com.innovation.backend.repository;

import com.innovation.backend.entity.DailyMission;
import com.innovation.backend.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findById(Long id);
    Mission findFirstByOrderByIdDesc();
    long count();

}
