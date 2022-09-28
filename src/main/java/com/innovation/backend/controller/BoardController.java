package com.innovation.backend.controller;

import com.innovation.backend.dto.response.LikeResultResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    //게시글 좋아요
    @PutMapping("/board/heart/{boardId}")
    public ResponseDto<LikeResultResponseDto> addBoardLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long boardId) {
        return ResponseDto.success(boardService.addBoardLike(userDetails,boardId));
    }

}