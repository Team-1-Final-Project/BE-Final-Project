package com.innovation.backend.domain.MainPage.controller;

import com.innovation.backend.domain.Badge.BadgeService;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.DailyMissionResponseDto;
import com.innovation.backend.domain.MainPage.service.MainService;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.domain.Meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final DailyMissionRepository dailyMissionRepository;
    private final MainService mainService;
    private final MemberRepository memberRepository;
    private final MeetingService meetingService;
    private final BadgeService badgeService;

    // 데일리 미션 조회
    @GetMapping("/main/daily")
    public ResponseDto<DailyMissionResponseDto> dailyMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        DailyMissionResponseDto dailyMissionResponseDto;
        dailyMissionResponseDto = mainService.dailyMission(userDetails);
        return ResponseDto.success(dailyMissionResponseDto);
    }

    // 데일리 미션 성공
    @PostMapping("/main/daily")
    public ResponseDto<DailyMissionResponseDto> successMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        DailyMissionResponseDto dailyMissionResponseDto;
        try {
            dailyMissionResponseDto = mainService.successMission(userDetails);
        } catch (
                CustomErrorException e) {
            log.error(e.getMessage());
            return ResponseDto.fail(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(ErrorCode.NEED_LOGIN);
        }
        badgeService.getMissionChallengerBadge(userDetails, "MissionChallenger");
        return ResponseDto.success(dailyMissionResponseDto);
    }

    // 신규 모임 조회
    @GetMapping("/main/newmeeting")
    public ResponseDto<List<MeetingResponseDto>> getNewMeeting() {
        List<MeetingResponseDto> meetingResponseDtoList;
        try {
            meetingResponseDtoList = mainService.getNewMeeting();
        } catch (Exception e) {
            return ResponseDto.fail(ErrorCode.INVALID_ERROR);
        }
        return ResponseDto.success(meetingResponseDtoList);
    }

    // 주간 인기글 조회
    @GetMapping("/main/hitboard")
    public ResponseDto<List<BoardResponseDto>> getHitBoard() {
        List<BoardResponseDto> boardResponseDtoList;
        try {
            boardResponseDtoList = mainService.getHitBoard();
        } catch (Exception e) {
            return ResponseDto.fail(ErrorCode.INVALID_ERROR);
        }
        return ResponseDto.success(boardResponseDtoList);

    }
}
