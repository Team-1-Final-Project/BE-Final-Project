package com.innovation.backend.domain.Search;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.dto.response.MeetingGetAllResponseDto;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SearchService {
  private final BoardRepository boardRepository;
  private final MeetingRepository meetingRepository;

  //게시글 검색

  public Slice<BoardResponseDto> searchBoard (String keyword , Pageable pageable){

    Page<Board> boardList = boardRepository.findByTitleContainsIgnoreCase(keyword,pageable);

    List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
    for(Board board : boardList){
      boardResponseDtoList.add(new BoardResponseDto(board));
    }
    return new PageImpl<>(boardResponseDtoList, pageable,boardList.getTotalElements());
  }

  //모임 검색
  public Slice<MeetingGetAllResponseDto> searchMeeting (String keyword, Pageable pageable){

    Page<Meeting> meetingList = meetingRepository.findByTitleContainsIgnoreCase(keyword,pageable);

    List<MeetingGetAllResponseDto> meetingGetAllResponseDtos = new ArrayList<>();
    for (Meeting meeting : meetingList){
      meetingGetAllResponseDtos.add(new MeetingGetAllResponseDto(meeting));
    }
    return new PageImpl<>(meetingGetAllResponseDtos, pageable,meetingList.getTotalElements());
  }
}
