package com.innovation.backend.domain.MainPage.service;

import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.MainBoardResponseDto;
import com.innovation.backend.domain.Comment.repository.CommentRepository;
import com.innovation.backend.domain.DailyMission.dto.response.DailyMissionResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.DailyMission.domain.DailyMission;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.security.UserDetailsImpl;
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
    private final CommentRepository commentRepository;

    LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
    LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));
    LocalDateTime weekStartDatetime = startDatetime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDateTime weekEndDatetime = endDatetime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    // ????????? ?????? ??????
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

    // ????????? ?????? ??????
    public DailyMissionResponseDto successMission(UserDetailsImpl userDetails) {

        String userEmail = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userEmail).orElseThrow();
        Boolean isDaily = dailyMissionRepository.existsByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        Long clearCount = 0L;
        DailyMission getDailyMission = dailyMissionRepository.findFirstByOrderByIdDesc();
        String missionContent = getDailyMission.getMission();
        DailyMission dailyMission = new DailyMission(missionContent, member);

        if (isDaily == false && userDetails != null) {
            dailyMissionRepository.save(dailyMission);
            clearCount = dailyMissionRepository.countByCreatedAtBetween(startDatetime, endDatetime) - 1;
        } else {
            // ?????? ????????? ????????? ??????????????? ????????????
            throw new CustomErrorException(ErrorCode.DUPLICATED_MISSION);
        }

        return new DailyMissionResponseDto(dailyMission, clearCount, isDaily);
    }

    // ?????? ?????? ??????
    public List<MeetingResponseDto> getNewMeeting() {
        List<Meeting> meetingList = meetingRepository.findTop4ByOrderByCreatedAtDesc();
        List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();

        for (Meeting meeting : meetingList) {
            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
            meetingResponseDtoList.add(meetingResponseDto);
        }

        return meetingResponseDtoList;
    }

    // ?????? ????????? ??????
    public List<MainBoardResponseDto> getHitBoard() {
        List<Board> boardList = boardRepository.findTop4ByCreatedAtBetweenOrderByHeartBoardNumsDesc(weekStartDatetime, weekEndDatetime);
        List<MainBoardResponseDto> boardResponseDtoList = new ArrayList<>();
        for (Board board : boardList) {
            int commentNums = commentRepository.countCommentsByBoard(board);
            MainBoardResponseDto boardResponseDto = new MainBoardResponseDto(board,commentNums);
            boardResponseDtoList.add(boardResponseDto);
        }
        return boardResponseDtoList;
    }
}
