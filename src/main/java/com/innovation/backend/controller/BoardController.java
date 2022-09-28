package com.innovation.backend.controller;

import com.innovation.backend.dto.request.BoardRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<?> getAllBoards() {
        return boardService.getAllBoard();
    }

    @PostMapping(value = "/board", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<?> createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart BoardRequestDto boardRequestDto, @RequestPart MultipartFile uploadImage) {
        return boardService.createBoard(userDetails, boardRequestDto, uploadImage);
    }

    @GetMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<?> getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping(value = "/board/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> alterBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart BoardRequestDto boardRequestDto, @RequestPart MultipartFile uploadImage {
        return boardService.alterBoard(id, userDetails, boardRequestDto, uploadImage);
    }

    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails);
    }





}
