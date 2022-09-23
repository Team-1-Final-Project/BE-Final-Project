package com.innovation.backend.service;

import com.innovation.backend.dto.request.MeetingRequestDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.CrewRepository;
import com.innovation.backend.repository.MeetingRepository;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.util.S3Upload;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final MemberRepository memberRepository;

  private final CrewRepository crewRepository;

  private final S3Upload s3Upload;

  //모임 생성
  @Transactional
  public void createMeeting (MeetingRequestDto requestDto, Member member, MultipartFile image){
    String meetingImage = null;

    if (!image.isEmpty()) {
      try {
        meetingImage = s3Upload.uploadFiles(image, "images");
        System.out.println(meetingImage);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    Meeting meeting = new Meeting(requestDto,member,meetingImage); // 모임 객체 생성
    Crew crew = new Crew(member,meeting);// 모임장 크루에 넣기

    //모임장 유저 정보 찾기
    Long memberId = member.getId();
    Member admin = memberRepository.findById(memberId).orElseThrow(
        () -> new CustomErrorException(ErrorCode.MEMBER_NOT_FOUND));

    //유저에 모임, 크루 추가
    List<Meeting> meetingList = admin.getMeetings();
    meetingList.add(meeting);
    List<Crew> crewList = admin.getCrews();
    crewList.add(crew);
    List<Crew> meetingCrewList = meeting.getCrew();
    meetingCrewList.add(crew);

    meetingRepository.save(meeting);
    crewRepository.save(crew);

  }

  //모임 수정
  @Transactional
  public void updateMeeting(Long meetingId, MeetingRequestDto requestDto, Member member) {
    //해당 모임 찾기
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //모임장과 같은 유저인지 확인하기
    if (meeting.isWrittenBy(member)) {
      meeting.update(requestDto);
    } else {
      throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
    }
  }

  //모임 이미지 수정
  @Transactional
  public void updateMeetingImage(Long meetingId,Member member ,MultipartFile image){
    //해당 모임 찾기
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    String meetingImage = meeting.getMeetingImage();
//모임장과 같은 유저인지 확인하기
    if (meeting.isWrittenBy(member)) {
      if (!image.isEmpty()) {
        try {
          s3Upload.fileDelete(meetingImage);
          meetingImage = s3Upload.uploadFiles(image, "images");
          System.out.println(meetingImage);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
    }
    meeting.updateMeetingImage(meetingImage);
  }


  //모임 삭제
  @Transactional
  public void deleteMeeting(Long meetingId, Member member) {
    //해당 모임 찾기
    Meeting meeting = meetingRepository.findById(meetingId)
        .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

    //모임장과 같은 유저인지 확인하기
    if (meeting.isWrittenBy(member)) {
      meetingRepository.delete(meeting);
    } else {
      throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
    }
  }

  //모임 이미지 삭제
@Transactional
public void deleteImage(Long meetingId,Member member) {
  //해당 모임 찾기
  Meeting meeting = meetingRepository.findById(meetingId)
      .orElseThrow(()-> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

  String meetingImage = meeting.getMeetingImage();

  //모임장과 같은 유저인지 확인하기
  if (meeting.isWrittenBy(member)) {
  meeting.deleteMeetingImage();//db에서 null로 바꿔줌
  s3Upload.fileDelete(meetingImage);//S3에서 사진 삭제
  } else {
    throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
  }

}


  //모임 전체 조회 (전체)
//  public List<MeetingResponseDto> getAllMeeting (){
//    List<Meeting> meetingList = meetingRepository.findAllByOrderByCreatedAtDesc();
//
//   return
//  }


  //모임 상세 조회 (전체)


  //모임 태그별 조회 (전체)


  //모임 좋아요 토글


  //좋아요한 모임만 조회 (사용자)


}
