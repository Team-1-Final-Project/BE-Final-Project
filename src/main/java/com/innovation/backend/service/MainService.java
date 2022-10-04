package com.innovation.backend.service;

import com.innovation.backend.dto.response.BoardResponseDto;
import com.innovation.backend.dto.response.DailyMissionResponseDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.DailyMission;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.repository.BoardRepository;
import com.innovation.backend.repository.DailyMissionRepository;
import com.innovation.backend.repository.MeetingRepository;
import com.innovation.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final DailyMissionRepository dailyMissionRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;
    private final BoardRepository boardRepository;

    LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
    LocalDateTime weekStartDatetime = startDatetime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDateTime weekEndDatetime = endDatetime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    // 데일리 미션 조회
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

    // 데일리 미션 성공
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

    // 신규 모임 조회
    public List<MeetingResponseDto> getNewMeeting() {
        List<Meeting> meetingList = meetingRepository.findTop4ByOrderByCreatedAtDesc();
        List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();

        for (Meeting meeting : meetingList) {
            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
            meetingResponseDtoList.add(meetingResponseDto);
        }

        return meetingResponseDtoList;
    }

    // 주간 인기글 조회
    public List<BoardResponseDto> getHitBoard() {
        List<Board> boardList = boardRepository.findTop4ByCreatedAtBetweenOrderByHeartBoardNumsDesc(weekStartDatetime, weekEndDatetime);
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);
            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }
}
