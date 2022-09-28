package com.innovation.backend.dto.response;

import com.innovation.backend.entity.DailyMission;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DailyMissionResponseDto {
    private String mission;
    private Long clearCount;
    private LocalDateTime createdAt;
    private boolean missionClear;
    public DailyMissionResponseDto(DailyMission dailyMission, Long clearCount, Boolean isDaily) {
        this.mission = dailyMission.getMission();
        this.createdAt = dailyMission.getCreatedAt();
        this.clearCount = clearCount;
        this.missionClear = isDaily;
    }

    public DailyMissionResponseDto(DailyMission dailyMission, Boolean isDaily) {
        this.mission = dailyMission.getMission();
        this.createdAt = dailyMission.getCreatedAt();
        this.missionClear = isDaily;
    }
}
