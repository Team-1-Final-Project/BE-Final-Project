package com.innovation.backend.controller;

import com.innovation.backend.dto.response.DailyMissionResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.DailyMissionRepository;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.service.DailyMissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {

    private final DailyMissionRepository dailyMissionRepository;
    private final DailyMissionService dailyMissionService;
    private final MemberRepository memberRepository;

    // 데일리 미션 조회
    @GetMapping("/main/daily")
    public ResponseDto<DailyMissionResponseDto> dailyMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        DailyMissionResponseDto dailyMissionResponseDto;
        dailyMissionResponseDto = dailyMissionService.dailyMission(userDetails);
        return ResponseDto.success(dailyMissionResponseDto);
    }

    // 데일리 미션 성공
    @PostMapping("/main/daily")
    public ResponseDto<DailyMissionResponseDto> successMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            DailyMissionResponseDto dailyMissionResponseDto;
            dailyMissionResponseDto = dailyMissionService.successMission(userDetails);
            return ResponseDto.success(dailyMissionResponseDto);
        } catch (
                CustomErrorException e) {
            log.error(e.getMessage());
            return ResponseDto.fail(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(ErrorCode.NEED_LOGIN);
        }

    }
}
