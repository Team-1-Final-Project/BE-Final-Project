package com.innovation.backend.controller;

import com.innovation.backend.dto.request.CommentRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.createComment(userDetails, commentRequestDto);
    }

    @PutMapping(value = "/comment/{commentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> alterComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.alterComment(commentId, userDetails, commentRequestDto);
    }

    @DeleteMapping("/comment/{commentId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails);
    }


}
