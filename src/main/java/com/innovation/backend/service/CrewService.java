package com.innovation.backend.service;

import com.innovation.backend.dto.response.CrewResponseDto;
import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.enums.MeetingStatus;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.repository.CrewRepository;
import com.innovation.backend.repository.MeetingRepository;
import com.innovation.backend.repository.MemberRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CrewService {

  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final MeetingRepository meetingRepository;

  //모임 참여
  @Transactional
  public void join(Long meetingId, Long memberId){
    Member member = memberRepository.findById(memberId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.MEMBER_NOT_FOUND));

    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //참여가능한 모임인지 확인
    isValidateMeeting(meeting);

    //이미 참여한 멤버인지 확인
    crewRepository.findByMemberAndMeeting(member,meeting)
        .ifPresent(e->{throw new CustomErrorException(ErrorCode.ALREADY_JOIN);});

    Crew crew = new Crew(member, meeting);
    meeting.addCrew(crew);
    meeting.addNowPeople();

    //모임 참여자 수가 정원과 같으면 모임 상태 변경
    if(meeting.getCrews().size() == meeting.getLimitPeople()){
      meeting.setMeetingStatus(MeetingStatus.COMPLETE_JOIN);
    }

    crewRepository.save(crew);
    meetingRepository.save(meeting);
  }

  //모임 참여 취소
  @Transactional
  public void cancelJoin(Long meetingId, Long memberId){
    Member member = memberRepository.findById(memberId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.MEMBER_NOT_FOUND));

    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //참여 안한 멤버인지 확인
    Crew crew = crewRepository.findByMemberAndMeeting(member,meeting)
        .orElseThrow(()->new CustomErrorException(ErrorCode.NEVER_JOIN));

    //모임장인 경우 참여취소 안됨
    if(meeting.getAdmin().equals(member)){
      throw new CustomErrorException(ErrorCode.ADMIN_CANNOT_CANCEL_JOIN);
    }

    //참여취소 가능한 모임인지 확인
    isValidateCancelMeeting(meeting);

    crewRepository.deleteByMemberAndMeeting(member,meeting);
    meeting.minusNowPeople();
    meeting.deleteCrew(crew);

    //참여자 수가 정원 보다 적은 경우 모임 상태 변경
    if(meeting.getCrews().size() < meeting.getLimitPeople()){
      meeting.setMeetingStatus(MeetingStatus.CAN_JOIN);
    }
    meetingRepository.save(meeting);
  }

  //모임 참여 유저 리스트 조회
  public List<CrewResponseDto> getCrewList (Long meetingId){
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    List<Crew> crewList = crewRepository.findByMeeting(meeting);
    List<CrewResponseDto> crewResponseDtoList = new ArrayList<>();

    for(Crew crew : crewList){
      CrewResponseDto crewResponseDto = new CrewResponseDto(crew);
      crewResponseDtoList.add(crewResponseDto);
    }
    return crewResponseDtoList;
  }

  // 모임참여시 모임 검증
  public void isValidateMeeting (Meeting meeting){
    if(meeting.getMeetingStatus() == MeetingStatus.COMPLETE_JOIN){
      throw new CustomErrorException(ErrorCode.ALREADY_COMPLETE_JOIN);
    }
    isValidateCancelMeeting(meeting);
  }

  //모임 참여 취소시
  public void isValidateCancelMeeting (Meeting meeting){
    if (meeting.getMeetingStatus() == MeetingStatus.PASS_DEADLINE){
      throw new CustomErrorException(ErrorCode.ALREADY_PASS_DEADLINE);
    }
    if(meeting.getMeetingStatus() == MeetingStatus.COMPLETED_MEETING){
      throw new CustomErrorException(ErrorCode.ALREADY_COMPLETED_MEETING);
    }
    if(meeting.getMeetingStatus() == MeetingStatus.READY_FOR_JOIN){
      throw new CustomErrorException(ErrorCode.READY_FOR_JOIN);
    }
    if(meeting.getJoinEndDate().isBefore(LocalDate.now())){
      throw new CustomErrorException(ErrorCode.ALREADY_PASS_DEADLINE);
    }
  }
}
