package com.innovation.backend.util;

import com.innovation.backend.entity.DailyMission;
import com.innovation.backend.entity.Mission;
import com.innovation.backend.repository.DailyMissionRepository;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DailyMissionScheduled {

    private final MissionRepository missionRepository;
    private final DailyMissionRepository dailyMissionRepository;
    private final MemberRepository memberRepository;
    Long missionId = Long.valueOf(0);
    LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

    @PostConstruct
    public void initMission() {

        Long count = missionRepository.count();
        if (count == 0) {
            String basicMission = "중고 거래하기";
            Mission mission = new Mission(basicMission);
            missionRepository.save(mission);
        }

        Mission mission = missionRepository.findFirstByOrderByIdDesc();
        String missionContent = mission.getMission();
        boolean isDaily = dailyMissionRepository.existsByMissionAndCreatedAtBetween(missionContent, startDatetime, endDatetime);
        if (isDaily == false) {
            DailyMission dailyMission = new DailyMission(missionContent, null);
            dailyMissionRepository.save(dailyMission);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void dailyMission() {

        Long count = missionRepository.count();
        if (missionId > count) {
            missionId = Long.valueOf(1);
        } else {
            missionId += 1;
        }

        Optional<Mission> mission = missionRepository.findById(missionId);
        String missionContent = mission.get().getMission();
        boolean isDaily = dailyMissionRepository.existsByMissionAndCreatedAtBetween(missionContent, startDatetime, endDatetime);
        if (isDaily == false) {
            DailyMission dailyMission = new DailyMission(missionContent, null);
            dailyMissionRepository.save(dailyMission);
        }

    }
}
