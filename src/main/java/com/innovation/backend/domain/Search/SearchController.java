package com.innovation.backend.domain.Search;

import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.service.BoardService;
import com.innovation.backend.domain.Meeting.dto.response.MeetingGetAllResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.global.common.response.ResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  //게시글 검색하기
  @GetMapping("/board/search")
  public ResponseDto<Slice<BoardResponseDto>> searchBoard (@RequestParam(value = "keyword") String keyword,@PageableDefault(size = 12) Pageable pageable) {
    return ResponseDto.success(searchService.searchBoard(keyword));
  }

  //모임검색하기
  @GetMapping("/meeting/search")
  public ResponseDto<Slice<MeetingGetAllResponseDto>> searchMeeting (@RequestParam(value = "keyword") String keyword, @PageableDefault(size = 12) Pageable pageable) {
    return ResponseDto.success(searchService.searchMeeting(keyword));
  }
}
