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
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class SearchService {
  private final BoardRepository boardRepository;
  private final MeetingRepository meetingRepository;

  //게시글 검색
  public List<BoardResponseDto> searchBoard (String keyword){

    List<Board> boardList = boardRepository.findByTitleContainsIgnoreCase(keyword);

    List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
    for(Board board : boardList){
      boardResponseDtoList.add(new BoardResponseDto(board));
    }
    return boardResponseDtoList;
  }

  //모임 검색
  public List<MeetingGetAllResponseDto> searchMeeting (String keyword){

    List<Meeting> meetingList = meetingRepository.findByTitleContainsIgnoreCase(keyword);

    List<MeetingGetAllResponseDto> meetingGetAllResponseDtos = new ArrayList<>();
    for (Meeting meeting : meetingList){
      meetingGetAllResponseDtos.add(new MeetingGetAllResponseDto(meeting));
    }
    return meetingGetAllResponseDtos;
  }
}
