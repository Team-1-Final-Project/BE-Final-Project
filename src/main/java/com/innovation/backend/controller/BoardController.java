package com.innovation.backend.controller;

import com.innovation.backend.dto.request.BoardRequestDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/board")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<?> createBoard(@RequestPart BoardRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) {
        return boardService.createBoard(requestDto, multipartFile, request);
    }

    @GetMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<?> getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping("/board/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> alterBoard(@PathVariable Long id, @RequestPart BoardRequestDto requestDto, @RequestPart MultipartFile multipartFile, HttpServletRequest request) {
        return boardService.alterBoard(id, requestDto, request);
    }

    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }





}
