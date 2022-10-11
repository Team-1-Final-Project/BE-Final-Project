package com.innovation.backend.domain.Board.controller;

import com.innovation.backend.domain.Board.dto.request.BoardRequestDto;
import com.innovation.backend.domain.Board.dto.request.TagBoardRequestDto;
import com.innovation.backend.domain.Board.dto.response.BoardLikeResponseDto;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.GetAllBoardDto;
import com.innovation.backend.domain.Board.service.BoardService;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    //게시글 좋아요
    @PutMapping("/board/heart/{boardId}")
    public ResponseDto<BoardLikeResponseDto> addBoardLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long boardId) {
        return ResponseDto.success(boardService.addBoardLike(userDetails,boardId));
    }

    //게시글 좋아요 확인
    @GetMapping("/board/heart/{boardId}")
    public ResponseDto<BoardLikeResponseDto> getBoardLike(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long boardId) {
        BoardLikeResponseDto likeResultResponseDto;
        try {
            likeResultResponseDto = boardService.getBoardLike(userDetails,boardId);
        } catch (
                CustomErrorException e) {
            log.error(e.getMessage());
            return ResponseDto.fail(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseDto.fail(ErrorCode.NEED_LOGIN);
        }
        return ResponseDto.success(likeResultResponseDto);
    }

    @GetMapping("/board")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Slice<GetAllBoardDto>> getAllBoards(Pageable pageable) {
        return boardService.getAllBoard(pageable);
    }

    @PostMapping(value = "/board", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<?> createBoard(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "data") BoardRequestDto boardRequestDto, @RequestPart(value = "boardImage")  MultipartFile uploadImage) throws IOException {
        return boardService.createBoard(userDetails, boardRequestDto, uploadImage);
    }

    @GetMapping("/board/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<?> getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PutMapping(value = "/board/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> alterBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestPart(value = "data") BoardRequestDto boardRequestDto, @RequestPart(value = "boardImage")  MultipartFile uploadImage) throws IOException {
        return boardService.alterBoard(id, userDetails, boardRequestDto, uploadImage);
    }

    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto<?> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(id, userDetails);
    }

    //게시글 태그별 조회
    @PostMapping("/board/tag")
    public ResponseDto<List<BoardResponseDto>> getBoardByTag(@RequestBody TagBoardRequestDto tagBoardRequestDto){
        return ResponseDto.success(boardService.getBoardByTag(tagBoardRequestDto));
    }

}
