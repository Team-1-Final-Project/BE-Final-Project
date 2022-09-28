package com.innovation.backend.service;

import com.innovation.backend.dto.response.DailyMissionResponseDto;
import com.innovation.backend.entity.DailyMission;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.DailyMissionRepository;
import com.innovation.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DailyMissionService {

    private final DailyMissionRepository dailyMissionRepository;
    private final MemberRepository memberRepository;

    LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

    public DailyMissionResponseDto dailyMission(UserDetailsImpl userDetails) {
        DailyMission dailyMission = dailyMissionRepository.findFirstByOrderByIdDesc();
        Boolean isDaily = false;
        Long clearCount = 0L;

        if (userDetails != null) {
            String userEmail = userDetails.getUsername();
            Member member = memberRepository.findByEmail(userEmail).orElseThrow();
            isDaily = dailyMissionRepository.existsByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        }

        if (dailyMission != null) {
            clearCount = dailyMissionRepository.countByCreatedAtBetween(startDatetime, endDatetime) - 1;
        }

        return new DailyMissionResponseDto(dailyMission, clearCount, isDaily);
    }


    public DailyMissionResponseDto successMission(UserDetailsImpl userDetails) {

        String userEmail = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow();
        Boolean isDaily = dailyMissionRepository.existsByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        DailyMission getDailyMission = dailyMissionRepository.findFirstByOrderByIdDesc();
        String missionContent = getDailyMission.getMission();
        DailyMission dailyMission = new DailyMission(missionContent, member);

        if (isDaily == false) {
            dailyMissionRepository.save(dailyMission);
            isDaily = true;
        } else {
            // 이미 성공한 미션을 요청할경우 예외처리
            throw new CustomErrorException(ErrorCode.DUPLICATED_MISSION);
        }

        return new DailyMissionResponseDto(dailyMission, isDaily);
    }
}
