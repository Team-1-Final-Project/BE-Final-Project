package com.innovation.backend.domain.Member.service;


import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.HeartBoard;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Board.repository.HeartBoardRepository;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Badge.Badge;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Badge.TagBadge;
import com.innovation.backend.domain.Member.dto.response.BadgeResponseDto;
import com.innovation.backend.domain.Badge.BadgeRepository;
import com.innovation.backend.domain.Badge.TagBadgeRepository;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@AllArgsConstructor
public class MyPageService {

  private final CrewRepository crewRepository;
  private final MeetingRepository meetingRepository;
  private final BoardRepository boardRepository;
  private final HeartBoardRepository heartBoardRepository;
  private final BadgeRepository badgeRepository;
  private final TagBadgeRepository tagBadgeRepository;
  //참여한 모임 조회
  public List<MeetingResponseDto> GetJoinMeeting (Member member){

    List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();
    List<Crew> crewList = crewRepository.findByMember(member);

    for(Crew crew : crewList) {
      Meeting meeting = meetingRepository.findById(crew.getMeeting().getId())
          .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

      MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
      meetingResponseDtoList.add(meetingResponseDto);
    }

    return meetingResponseDtoList;
  }

    //작성한 게시글 조회
    public List<BoardResponseDto> getWriteBoard(Member member) {
      List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
      List<Board> boardList = boardRepository.findByMember(member);

      for (Board board : boardList) {
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        boardResponseDtoList.add(boardResponseDto);
      }

      return boardResponseDtoList;
    }

    //좋아요한 게시글 조회
    public List<BoardResponseDto> getMyHitBoard(Member member) {
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();
        List<HeartBoard> heartBoardList = heartBoardRepository.findByMember(member);

        for (HeartBoard heartBoard : heartBoardList) {
            Board board = boardRepository.findById(heartBoard.getBoard().getId()).orElseThrow();
            BoardResponseDto boardResponseDto = new BoardResponseDto(board);
            boardResponseDtoList.add(boardResponseDto);
        }

        return boardResponseDtoList;
    }

    public List<BadgeResponseDto> getMyBadge(Member member) {
        List<BadgeResponseDto> badgeResponseDtoList = new ArrayList<>();
        List<Badge> badgeList = badgeRepository.findByMember(member);
        for (Badge badge : badgeList) {
            TagBadge tagBadge = tagBadgeRepository.findById(badge.getTagBadge().getId()).orElseThrow();
            BadgeResponseDto badgeResponseDto = new BadgeResponseDto(tagBadge);
            badgeResponseDtoList.add(badgeResponseDto);
        }
        return badgeResponseDtoList;
    }
}
