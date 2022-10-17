package com.innovation.backend.domain.Member.service;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.HeartBoard;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.DailyMission.domain.DailyMission;
import com.innovation.backend.domain.DailyMission.dto.response.DailyMissionResponseDto;
import com.innovation.backend.domain.DailyMission.dto.response.MissionClearResponseDto;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.domain.Review.domain.Review;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Review.dto.ReviewResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.Board.repository.HeartBoardRepository;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MyPageService {

  private final MemberRepository memberRepository;
  private final CrewRepository crewRepository;
  private final MeetingRepository meetingRepository;
  private final BoardRepository boardRepository;
  private final HeartBoardRepository heartBoardRepository;
  private final ReviewRepository reviewRepository;
  private final DailyMissionRepository dailyMissionRepository;

  //참여한 모임 조회
  public List<MeetingResponseDto> GetJoinMeeting (UserDetailsImpl userDetails){
    //로그인 유저 정보 가져오기
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

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

  //작성한 후기 조회
  public List<ReviewResponseDto> getMyReview (UserDetailsImpl userDetails){
    //로그인 유저 정보 가져오기
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

    List<Review> reviews = reviewRepository.findByMember(member);
    List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();

    for(Review review : reviews){
      ReviewResponseDto reviewResponseDto = new ReviewResponseDto(review);
      reviewResponseDtos.add(reviewResponseDto);
    }
    return reviewResponseDtos;
  }

  //완료한 미션 조회
  public List<MissionClearResponseDto> getSuccessMission(Member member) {
    List<MissionClearResponseDto> missionClearResponseDtoList = new ArrayList<>();
    List<DailyMission> dailyMissionList = dailyMissionRepository.findByMember(member);
    for(DailyMission dailyMission : dailyMissionList){
      MissionClearResponseDto missionClearResponseDto = new MissionClearResponseDto(dailyMission);
      missionClearResponseDtoList.add(missionClearResponseDto);
    }
    return missionClearResponseDtoList;
  }
}
