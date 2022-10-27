package com.innovation.backend.domain.Member.service;


import com.innovation.backend.domain.Badge.domain.Badge;
import com.innovation.backend.domain.Badge.domain.TagBadge;
import com.innovation.backend.domain.Badge.dto.SignatureBadgeRequestDto;
import com.innovation.backend.domain.Badge.repository.BadgeRepository;
import com.innovation.backend.domain.Badge.repository.TagBadgeRepository;
import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.HeartBoard;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.MainBoardResponseDto;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Board.repository.HeartBoardRepository;
import com.innovation.backend.domain.Comment.repository.CommentRepository;
import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.DailyMission.domain.DailyMission;
import com.innovation.backend.domain.DailyMission.dto.response.MissionClearResponseDto;
import com.innovation.backend.domain.DailyMission.repository.DailyMissionRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.dto.response.MeetingGetAllResponseDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.dto.request.UserProfileRequestDto;
import com.innovation.backend.domain.Member.dto.response.BadgeResponseDto;
import com.innovation.backend.domain.Badge.repository.BadgeRepository;
import com.innovation.backend.domain.Badge.repository.TagBadgeRepository;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.domain.Review.repository.ReviewRepository;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import java.io.IOException;
import com.innovation.backend.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import com.innovation.backend.global.util.S3Upload;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@Slf4j
@AllArgsConstructor
public class MyPageService {

  private final CrewRepository crewRepository;
  private final MeetingRepository meetingRepository;
  private final BoardRepository boardRepository;
  private final HeartBoardRepository heartBoardRepository;
  private final DailyMissionRepository dailyMissionRepository;
  private final BadgeRepository badgeRepository;
  private final TagBadgeRepository tagBadgeRepository;
  private final MemberRepository memberRepository;
  private final CommentRepository commentRepository;
  private final S3Upload s3Upload;

  //참여한 모임 조회
  public List<MeetingGetAllResponseDto> GetJoinMeeting (UserDetailsImpl userDetails){
    Member member = memberRepository.findById(userDetails.getMember().getId())
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

    List<MeetingGetAllResponseDto> meetingGetAllResponseDtoList = new ArrayList<>();
    List<Crew> crewList = crewRepository.findByMember(member);

    for(Crew crew : crewList) {
      Meeting meeting = meetingRepository.findById(crew.getMeeting().getId())
          .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

      MeetingGetAllResponseDto meetingGetAllResponseDto = new MeetingGetAllResponseDto(meeting);
      meetingGetAllResponseDtoList.add(meetingGetAllResponseDto);
    }
    return meetingGetAllResponseDtoList;
  }

    //작성한 게시글 조회
    public List<MainBoardResponseDto> getWriteBoard(Member member) {
      List<MainBoardResponseDto> boardResponseDtoList = new ArrayList<>();
      List<Board> boardList = boardRepository.findByMember(member);

      for (Board board : boardList) {
          int commentNums = commentRepository.countCommentsByBoard(board);
          MainBoardResponseDto boardResponseDto = new MainBoardResponseDto(board,commentNums);
          boardResponseDtoList.add(boardResponseDto);
      }

      return boardResponseDtoList;
    }

    //좋아요한 게시글 조회
    public List<MainBoardResponseDto> getMyHitBoard(Member member) {
        List<MainBoardResponseDto> boardResponseDtoList = new ArrayList<>();
        List<HeartBoard> heartBoardList = heartBoardRepository.findByMember(member);

        for (HeartBoard heartBoard : heartBoardList) {
            Board board = boardRepository.findById(heartBoard.getBoard().getId()).orElseThrow();
            int commentNums = commentRepository.countCommentsByBoard(board);
            MainBoardResponseDto boardResponseDto = new MainBoardResponseDto(board,commentNums);
            boardResponseDtoList.add(boardResponseDto);
        }

        return boardResponseDtoList;
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

    public List<BadgeResponseDto> getMyBadge(Member member) {
        List<BadgeResponseDto> badgeResponseDtoList = new ArrayList<>();
        List<Badge> badgeList = badgeRepository.findByMember(member);
        for (Badge badge : badgeList) {
            TagBadge tagBadge = tagBadgeRepository.findById(badge.getTagBadge().getId()).orElseThrow();
            BadgeResponseDto badgeResponseDto = new BadgeResponseDto(tagBadge,badge);
            badgeResponseDtoList.add(badgeResponseDto);
        }
        return badgeResponseDtoList;
    }

    public void setUserprofile(UserDetailsImpl userDetails, MultipartFile image) {
        Member member = userDetails.getMember();
        String profileImage = member.getProfileImage();
        if (image != null && !image.isEmpty()) {
            try {
                profileImage = s3Upload.uploadFiles(image, "boardImages");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            profileImage = "https://test-bucket-jaewon.s3.ap-northeast-2.amazonaws.com/images/basic.jpg";
        }
        member.setProfileImage(profileImage);
        memberRepository.save(member);
    }

    public void setSignatureBadge(UserDetailsImpl userDetails, SignatureBadgeRequestDto badgeId) {
        Member member = userDetails.getMember();
        List<Badge> badgeList = badgeRepository.findByMember(member);
        for (Badge badge : badgeList) {
            badge.setSignatureBadge(false);
        }
        TagBadge tagBadge = tagBadgeRepository.findById(badgeId.getBadgeId()).orElseThrow();
        Badge badge = badgeRepository.findByMemberAndTagBadge(member,tagBadge);
        badge.setSignatureBadge(true);
        badgeRepository.save(badge);
    }
}
