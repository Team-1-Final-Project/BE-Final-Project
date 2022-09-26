package com.innovation.backend.service;

import com.innovation.backend.dto.response.CrewResponseDto;
import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.repository.CrewRepository;
import com.innovation.backend.repository.MeetingRepository;
import com.innovation.backend.repository.MemberRepository;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CrewService {

  private final CrewRepository crewRepository;
  private final MemberRepository memberRepository;
  private final MeetingRepository meetingRepository;

  //모임 참여하기
  @Transactional
  public CrewResponseDto join(Long meetingId, Long memberId){
    Member member = memberRepository.findById(memberId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.MEMBER_NOT_FOUND));

    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //이미 참여한 멤버인지 확인
    crewRepository.findByMemberAndMeeting(member,meeting)
        .ifPresent(e->{throw new CustomErrorException(ErrorCode.ALREADY_JOIN);});

    Crew crew = new Crew(member, meeting);
    meeting.addCrew(crew);
    meeting.addNowPeople();

    crewRepository.save(crew);

    return new CrewResponseDto(crew);

  }

  //모임 참여 취소하기
  @Transactional
  public void cancelJoin(Long meetingId, Long memberId){
    Member member = memberRepository.findById(memberId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.MEMBER_NOT_FOUND));

    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //참여 안한 멤버인지 확인
    Crew crew = crewRepository.findByMemberAndMeeting(member,meeting)
        .orElseThrow(()->new CustomErrorException(ErrorCode.NEVER_JOIN));

    crewRepository.deleteByMemberAndMeeting(member,meeting);
    meeting.minusNowPeople();
    meeting.deleteCrew(crew);

  }

  //모임 참여 인원 불러오기

  //모임 참여 유저 조회하기

  //출석 체크하기?
}
