package com.innovation.backend.controller;

import com.innovation.backend.dto.request.BoardRequestDto;
import com.innovation.backend.dto.request.CommentRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/comment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "data") CommentRequestDto commentRequestDto) {
        return commentService.createComment(userDetails, commentRequestDto);
    }

    @PutMapping(value = "/comment/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> alterComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "data") CommentRequestDto commentRequestDto) {
        return commentService.alterComment(commentId, userDetails, commentRequestDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }


}
