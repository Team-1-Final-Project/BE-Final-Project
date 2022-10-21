package com.innovation.backend.domain.MeetingComment.controller;

import com.innovation.backend.domain.MeetingComment.domain.MeetingComment;
import com.innovation.backend.domain.MeetingComment.dto.request.MeetingCommentRequestDto;
import com.innovation.backend.domain.MeetingComment.dto.response.MeetingCommentResponseDto;
import com.innovation.backend.domain.MeetingComment.service.MeetingCommentService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MeetingCommentController {

    private final MeetingCommentService meetingCommentService;

    @PostMapping(value = "/meetingComment/{meetingId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<MeetingCommentResponseDto> createMeetingComment(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MeetingCommentRequestDto meetingCommentRequestDto) {
        return meetingCommentService.createComment(meetingId, userDetails, meetingCommentRequestDto);
    }

    @GetMapping(value = "/meetingComment/{meetingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<List<MeetingCommentResponseDto>> getAllCommentsByMeeting(@PathVariable Long meetingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return meetingCommentService.getAllComments(meetingId, userDetails);
    }

    @PutMapping(value = "/meetingComment/{meetingCommentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<MeetingCommentResponseDto> alterMeetingComment(@PathVariable Long meetingCommentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody MeetingCommentRequestDto meetingCommentRequestDto) {
        return meetingCommentService.alterComment(meetingCommentId, userDetails, meetingCommentRequestDto);
    }

    @DeleteMapping(value = "/meetingComment/{meetingCommentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<String> deleteComment(@PathVariable Long meetingCommentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return meetingCommentService.deleteComment(meetingCommentId, userDetails);
    }
}
