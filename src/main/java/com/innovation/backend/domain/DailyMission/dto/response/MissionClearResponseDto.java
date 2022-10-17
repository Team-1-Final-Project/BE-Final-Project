package com.innovation.backend.domain.DailyMission.dto.response;

import com.innovation.backend.domain.DailyMission.domain.DailyMission;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class MissionClearResponseDto {

    private String mission;
    private LocalDateTime createdAt;

    public MissionClearResponseDto(DailyMission dailyMission) {
        this.mission = dailyMission.getMission();
        this.createdAt = dailyMission.getCreatedAt();
    }
}


