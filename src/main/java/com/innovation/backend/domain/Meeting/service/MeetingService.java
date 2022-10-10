package com.innovation.backend.domain.Meeting.service;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Meeting.domain.HeartMeeting;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.domain.MeetingTagConnection;
import com.innovation.backend.domain.Meeting.domain.TagMeeting;
import com.innovation.backend.domain.Meeting.dto.response.MeetingLikeResponseDto;
import com.innovation.backend.domain.Meeting.dto.request.MeetingRequestDto;
import com.innovation.backend.domain.Meeting.dto.response.MeetingResponseDto;
import com.innovation.backend.domain.Meeting.dto.request.TagMeetingRequestDto;
import com.innovation.backend.domain.Meeting.repository.HeartMeetingRepository;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.Meeting.repository.MeetingTagConnectionRepository;
import com.innovation.backend.domain.Meeting.repository.TagMeetingRepository;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.enums.MeetingStatus;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.global.util.S3Upload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final CrewRepository crewRepository;
    private final S3Upload s3Upload;

    private final TagMeetingRepository tagMeetingRepository;
    private final MemberRepository memberRepository;
    private final HeartMeetingRepository heartMeetingRepository;
    private final MeetingTagConnectionRepository meetingTagConnectionRepository;


    //모임 생성
    @Transactional
    public MeetingResponseDto createMeeting(MeetingRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile image) {
        //로그인 유저 정보 가져오기
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

        // 모집 기한, 모임 기한 로직
        isValidateDate(requestDto);

        //모집인원 검증 로직
        isValidatePeopleNumber(requestDto);

        String meetingImage = null;

        if (image != null && !image.isEmpty()) {
            try {
                meetingImage = s3Upload.uploadFiles(image, "images");
                log.info(meetingImage);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        Meeting meeting = new Meeting(requestDto, member, meetingImage);// 모임 객체 생성
        Crew crew = new Crew(member, meeting);// 모임장 크루에 넣기
        meeting.addCrew(crew);

        //모임 태그 추가
        addMeetingTagConnection(requestDto, meeting);

        meetingRepository.save(meeting);
        crewRepository.save(crew);
        return new MeetingResponseDto(meeting);
    }

    //모임 수정
    @Transactional
    public void updateMeeting(Long meetingId, MeetingRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile image) {
        //로그인 유저 정보 가져오기
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));
        //해당 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));
        // 수정 가능한 모임인지 확인
        isValidateStatus(meeting);


        //모임장과 같은 유저인지 확인하기
        if (meeting.isWrittenBy(member)) {

            //모집인원 검증 로직
            isValidatePeopleNumber(requestDto);

            // 모집 기한, 모임 기한 로직
            isValidateDate(requestDto);

            // 기존 태그와 비교
            Set<MeetingTagConnection> oldMeetingTagConnectionList = meeting.getMeetingTagConnectionList();

            // 새로운 tag id 리스트
            List<Long> newTagMeetingIdList = requestDto.getTagMeetingIds();

            // 기존 태그 커넥션 id list
            List<Long> oldMeetingTagConnectionIdList = new ArrayList<>();
            for (MeetingTagConnection meetingTagConnection : oldMeetingTagConnectionList) {
                oldMeetingTagConnectionIdList.add(meetingTagConnection.getId());
            }

            //기존 태그 id List
            List<Long> oldTagMeetingIdList = new ArrayList<>();
            for(Long oldMeetingTagConnectionId : oldMeetingTagConnectionIdList){
                MeetingTagConnection oldMeetingConnection = meetingTagConnectionRepository.findById(oldMeetingTagConnectionId).orElseThrow();
                oldTagMeetingIdList.add(oldMeetingConnection.getTagMeeting().getId());
            }


            for (MeetingTagConnection oldMeetingTagConnection : oldMeetingTagConnectionList) {
                if (!newTagMeetingIdList.equals(oldTagMeetingIdList)) {
                    for (Long newMeetingTagId : newTagMeetingIdList) {
                        if (oldMeetingTagConnection.getTagMeeting().getId().equals(newMeetingTagId)) {
                            // newMeetingTagIdList에 남아 있는 건 추가할 태그
                            newTagMeetingIdList.remove(newMeetingTagId);
                            // oldMeetingTagIdList에 남아 있는 건 삭제할 태그
                            oldMeetingTagConnectionIdList.remove(oldMeetingTagConnection.getId());
                        }}}}

            // 삭제
            for (Long oldMeetingTagId : oldMeetingTagConnectionIdList) {
                MeetingTagConnection deleteMeetingTagConnection = findTagId(oldMeetingTagConnectionList, oldMeetingTagId);
                if (deleteMeetingTagConnection != null) {
                    oldMeetingTagConnectionList.remove(deleteMeetingTagConnection);
                    meetingTagConnectionRepository.delete(deleteMeetingTagConnection);
                }}

            // 추가
            for (Long newMeetingTagId : newTagMeetingIdList) {
                TagMeeting tagMeeting
                    = tagMeetingRepository.findById(newMeetingTagId)
                    .orElseThrow(() -> new CustomErrorException(ErrorCode.ENTITY_NOT_FOUND));

                MeetingTagConnection meetingTagConnection = new MeetingTagConnection(meeting, tagMeeting);
                meetingTagConnection = meetingTagConnectionRepository.save(meetingTagConnection);
                oldMeetingTagConnectionList.add(meetingTagConnection);
            }
            meeting.setMeetingTagConnectionList(oldMeetingTagConnectionList);

            String meetingImage = meeting.getMeetingImage();

            if(meetingImage != null ){
                if(image == null || image.isEmpty()) {
                    meetingImage = meeting.getMeetingImage();
                }else if (!image.isEmpty()) {
                    try{
                        s3Upload.fileDelete(meetingImage);
                        meetingImage = s3Upload.uploadFiles(image, "images");
                    }catch (IOException e){
                        log.error(e.getMessage());
                    }
                }
            }else {
                if (image == null || image.isEmpty()) {
                    meetingImage = null;
                } else if (!image.isEmpty()) {
                    try {
                        meetingImage = s3Upload.uploadFiles(image, "images");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            // 수정
            meeting.update(requestDto,meetingImage);

        } else {
            throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
        }
    }

    private MeetingTagConnection findTagId(Set<MeetingTagConnection> meetingTagConnectionList, Long meetingTagConnectionId) {
        for (MeetingTagConnection meetingTagConnection : meetingTagConnectionList) {
            if (meetingTagConnection.getId().equals(meetingTagConnectionId)) {
                return meetingTagConnection;
            }
        }return null;
    }

    //모임 삭제
    @Transactional
    public void deleteMeeting(Long meetingId, UserDetailsImpl userDetails) {
        //로그인 유저 정보 가져오기
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));
        //해당 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        //모임장과 같은 유저인지 확인하기
        if (meeting.isWrittenBy(member)) {
            meetingRepository.delete(meeting);
        } else {
            throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
        }
    }

    //모임 전체 조회 (전체)
    public List<MeetingResponseDto> getAllMeeting() {

        List<Meeting> meetingList = meetingRepository.findAllByOrderByCreatedAtDesc();
        List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();

        for (Meeting meeting : meetingList) {
            MeetingResponseDto meetingResponseDto = new MeetingResponseDto(meeting);
            meetingResponseDtoList.add(meetingResponseDto);
        }

        return meetingResponseDtoList;
    }


    //모임 상세 조회 (전체)
    public MeetingResponseDto getMeeting(Long meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
            () -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        return new MeetingResponseDto(meeting);
    }

    // 모임 태그별 조회 (전체)
    public List<MeetingResponseDto> getMeetingByTag(TagMeetingRequestDto tagMeetingRequestDto) {
        Set<Meeting> meetings = new HashSet<>();

        for (Long tagId : tagMeetingRequestDto.getTagIds()) {
            List<MeetingTagConnection> meetingTagConnectionList = meetingTagConnectionRepository.findByTagId(tagId);

            for (MeetingTagConnection meetingTagConnection : meetingTagConnectionList) {
                meetings.add(meetingTagConnection.getMeeting());
            }
        }

        List<MeetingResponseDto> meetingResponseDtoList = new ArrayList<>();
        for (Meeting meeting : meetings) {
            meetingResponseDtoList.add(new MeetingResponseDto(meeting));
        }

        return meetingResponseDtoList;
    }


    private void addMeetingTagConnection(MeetingRequestDto requestDto, Meeting meeting) {
        Set<MeetingTagConnection> meetingTagConnectionList = new HashSet<>();
        for (Long tagId : requestDto.getTagMeetingIds()) {
            TagMeeting tagMeeting = tagMeetingRepository.findById(tagId)
                .orElseThrow(NullPointerException::new);
            MeetingTagConnection meetingTagConnection = new MeetingTagConnection(meeting, tagMeeting);
            meetingTagConnection = meetingTagConnectionRepository.save(meetingTagConnection);
            meetingTagConnectionList.add(meetingTagConnection);
        }
        meeting.setMeetingTagConnectionList(meetingTagConnectionList);
    }

    //모임 좋아요
    @Transactional
    public MeetingLikeResponseDto addMeetingLike(UserDetailsImpl userDetails, Long meetingId) {
        String userId = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userId).orElseThrow();
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
        Long likeNums = meeting.getHeartNums();
        boolean meetingLike = isMeetingLike(member,meeting);
        HeartMeeting heartMeeting = new HeartMeeting(member, meeting);
        if (isMeetingLike(member, meeting)) {
            heartMeetingRepository.deleteByMemberAndMeeting(member, meeting);
            meeting.addMeetingLike(likeNums - 1);
            return new MeetingLikeResponseDto(!meetingLike);
        } else {
            heartMeetingRepository.save(heartMeeting);
            meeting.addMeetingLike(likeNums + 1);
            return new MeetingLikeResponseDto(!meetingLike);
        }
    }

    // 좋아요 중복방지
    private boolean isMeetingLike(Member member, Meeting meeting) {
        return heartMeetingRepository.existsByMemberAndMeeting(member, meeting);
    }

    //모임 좋아요 여부확인
    @Transactional
    public MeetingLikeResponseDto getMeetingLike(UserDetailsImpl userDetails, Long meetingId) {
        boolean meetingLike = false;
        if (userDetails != null) {
            String userId = userDetails.getUsername();
            Member member = memberRepository.findByEmail(userId).orElseThrow();
            Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
            meetingLike = isMeetingLike(member,meeting);
        }
        return new MeetingLikeResponseDto(meetingLike);
    }

    //모집 날짜, 모임 날짜 검증
    private void isValidateDate (MeetingRequestDto requestDto){
        if (!requestDto.getJoinEndDate().isAfter(requestDto.getJoinStartDate())){
            throw new CustomErrorException(ErrorCode.WRONG_JOIN_DATE);
        }if(!requestDto.getJoinEndDate().isBefore(requestDto.getMeetingStartDate())){
            throw new CustomErrorException(ErrorCode.WRONG_DATE);
        }if(!(requestDto.getMeetingEndDate().isAfter(requestDto.getMeetingStartDate())||requestDto.getMeetingEndDate().isEqual(requestDto.getMeetingStartDate()))){
            throw new CustomErrorException(ErrorCode.WRONG_MEETING_DATE);
        }
    }

    // 모집 정원 현재 정원 검증
    private void isValidatePeopleNumber (MeetingRequestDto requestDto){
        if(requestDto.getLimitPeople()  <= 1){
            throw new CustomErrorException(ErrorCode.WRONG_LIMIT_PEOPLE);
        }
    }

    //수정 가능한 모임 상태 인지 확인
    private void isValidateStatus (Meeting meeting){
        if(!(meeting.getMeetingStatus() == MeetingStatus.CAN_JOIN || meeting.getMeetingStatus() == MeetingStatus.READY_FOR_JOIN)){
            if(meeting.getNowPeople() > 1){
                throw new CustomErrorException(ErrorCode.CAN_NOT_UPDATE_MEETING);
            }throw new CustomErrorException(ErrorCode.CAN_NOT_UPDATE_MEETING2);
        }
    }
}
