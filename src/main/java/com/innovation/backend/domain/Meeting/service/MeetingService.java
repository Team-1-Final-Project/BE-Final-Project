package com.innovation.backend.domain.Meeting.service;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Meeting.domain.HeartMeeting;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.domain.MeetingTagConnection;
import com.innovation.backend.domain.Meeting.domain.TagMeeting;
import com.innovation.backend.domain.Meeting.dto.response.MeetingGetAllResponseDto;
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
import java.util.stream.Collectors;
import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
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


    //?????? ??????
    @Transactional
    public MeetingResponseDto createMeeting(MeetingRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile image) {
        //????????? ?????? ?????? ????????????
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));

        // ?????? ??????, ?????? ?????? ??????
        isValidateDate(requestDto);

        //???????????? ?????? ??????
        isValidatePeopleNumber(requestDto);

        String meetingImage = null;
        String meetingThumbImage = null;

        if (image != null && !image.isEmpty()) {
            log.info("image file is exist");

            try {
                //?????? ????????? ??????
                meetingImage = s3Upload.uploadFiles(image, "images");
                log.info(meetingImage);
                //????????? ????????? ??????
                meetingThumbImage = s3Upload.uploadThumbFile(image, "thumbs");
                log.info(meetingThumbImage);
            } catch (Exception e) {
                log.error("image Upload fail :" , e);
            }
        }

        Meeting meeting = new Meeting(requestDto, member, meetingImage,meetingThumbImage);// ?????? ?????? ??????
        Crew crew = new Crew(member, meeting);// ????????? ????????? ??????
        meeting.addCrew(crew);

        //?????? ?????? ??????
        addMeetingTagConnection(requestDto, meeting);

        meetingRepository.save(meeting);
        crewRepository.save(crew);
        return new MeetingResponseDto(meeting);
    }

    //?????? ??????
    @Transactional
    public void updateMeeting(Long meetingId, MeetingRequestDto requestDto, UserDetailsImpl userDetails,MultipartFile image) {
        //????????? ?????? ?????? ????????????
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));
        //?????? ?????? ??????
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));
        // ?????? ????????? ???????????? ??????
        isValidateStatus(meeting);


        //???????????? ?????? ???????????? ????????????
        if (meeting.isWrittenBy(member)) {

            //???????????? ?????? ??????
            isValidatePeopleNumber(requestDto);

            // ?????? ??????, ?????? ?????? ??????
            isValidateDate(requestDto);

            // ?????? ????????? ??????
            Set<MeetingTagConnection> oldMeetingTagConnectionList = meeting.getMeetingTagConnectionList();

            // ????????? tag id ?????????
            List<Long> newTagMeetingIdList = requestDto.getTagMeetingIds();

            // ?????? ?????? ????????? id list
            List<Long> oldMeetingTagConnectionIdList = new ArrayList<>();
            for (MeetingTagConnection meetingTagConnection : oldMeetingTagConnectionList) {
                oldMeetingTagConnectionIdList.add(meetingTagConnection.getId());
            }

            //?????? ?????? id List
            List<Long> oldTagMeetingIdList = new ArrayList<>();
            for(Long oldMeetingTagConnectionId : oldMeetingTagConnectionIdList){
                MeetingTagConnection oldMeetingConnection = meetingTagConnectionRepository.findById(oldMeetingTagConnectionId).orElseThrow();
                oldTagMeetingIdList.add(oldMeetingConnection.getTagMeeting().getId());
            }


            for (MeetingTagConnection oldMeetingTagConnection : oldMeetingTagConnectionList) {
                if (!newTagMeetingIdList.equals(oldTagMeetingIdList)) {
                    for (Long newMeetingTagId : newTagMeetingIdList) {
                        if (oldMeetingTagConnection.getTagMeeting().getId().equals(newMeetingTagId)) {
                            // newMeetingTagIdList??? ?????? ?????? ??? ????????? ??????
                            newTagMeetingIdList.remove(newMeetingTagId);
                            // oldMeetingTagIdList??? ?????? ?????? ??? ????????? ??????
                            oldMeetingTagConnectionIdList.remove(oldMeetingTagConnection.getId());
                        }}}}

            // ??????
            for (Long oldMeetingTagId : oldMeetingTagConnectionIdList) {
                MeetingTagConnection deleteMeetingTagConnection = findTagId(oldMeetingTagConnectionList, oldMeetingTagId);
                if (deleteMeetingTagConnection != null) {
                    oldMeetingTagConnectionList.remove(deleteMeetingTagConnection);
                    meetingTagConnectionRepository.delete(deleteMeetingTagConnection);
                }}

            // ??????
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
            String meetingThumbImage = meeting.getMeetingThumbImage();

            if(meetingImage != null ){
                if(image == null || image.isEmpty()) {
                    meetingImage = meeting.getMeetingImage();
                    meetingThumbImage = meeting.getMeetingThumbImage();
                }else if (!image.isEmpty()) {
                    try{
                        s3Upload.fileDelete(meetingImage);
                        s3Upload.fileDelete(meetingThumbImage);
                        meetingImage = s3Upload.uploadFiles(image, "images");
                        meetingThumbImage = s3Upload.uploadThumbFile(image, "thumbs");
                    }catch (IOException e){
                        log.error(e.getMessage());
                    }
                }
            }else {
                if (image == null || image.isEmpty()) {
                    meetingImage = null;
                    meetingThumbImage = null;
                } else if (!image.isEmpty()) {
                    try {
                        meetingImage = s3Upload.uploadFiles(image, "images");
                        meetingThumbImage = s3Upload.uploadThumbFile(image, "thumbs");
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
            // ??????
            meeting.update(requestDto,meetingImage,meetingThumbImage);

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

    //?????? ??????
    @Transactional
    public void deleteMeeting(Long meetingId, UserDetailsImpl userDetails) {
        //????????? ?????? ?????? ????????????
        Member member = memberRepository.findById(userDetails.getMember().getId())
            .orElseThrow(()-> new CustomErrorException(ErrorCode.NEED_LOGIN));
        //?????? ?????? ??????
        Meeting meeting = meetingRepository.findById(meetingId)
            .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        //???????????? ?????? ???????????? ????????????
        if (meeting.isWrittenBy(member)) {
            meetingRepository.delete(meeting);
            if(meeting.getMeetingImage() != null && meeting.getMeetingThumbImage() != null){
                s3Upload.fileDelete(meeting.getMeetingImage());
                s3Upload.fileDelete(meeting.getMeetingThumbImage());
            }
        } else {
            throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
        }
    }

    //?????? ?????? ?????? (??????)
    public Page<MeetingGetAllResponseDto> getAllMeeting(Pageable pageable) {

        Page<Meeting> meetingList = meetingRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<MeetingGetAllResponseDto> meetingGetAllResponseDtoList = new ArrayList<>();

        for (Meeting meeting : meetingList) {
            MeetingGetAllResponseDto meetingGetAllResponseDto = new MeetingGetAllResponseDto(meeting);
            meetingGetAllResponseDtoList.add(meetingGetAllResponseDto);
        }

        return new PageImpl<>(meetingGetAllResponseDtoList, pageable, meetingList.getTotalElements());
    }


    //?????? ?????? ?????? (??????)
    public MeetingResponseDto getMeeting(Long meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
            () -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        return new MeetingResponseDto(meeting);
    }

    // ?????? ????????? ?????? (??????)
    public Page<MeetingGetAllResponseDto> getMeetingByTag(TagMeetingRequestDto tagMeetingRequestDto, Pageable pageable) {
        Long totalElement = meetingRepository.findByTagIdCount(tagMeetingRequestDto.getTagIds());

        List<Meeting> meetingList =
            meetingRepository.findByTagId(tagMeetingRequestDto.getTagIds(), pageable.getOffset(), pageable.getPageSize());

        List<MeetingGetAllResponseDto> meetingGetAllResponseDtoList = new ArrayList<>();

        for (Meeting meeting : meetingList) {
            meetingGetAllResponseDtoList.add(new MeetingGetAllResponseDto(meeting));
        }

        return new PageImpl<>(meetingGetAllResponseDtoList, pageable, totalElement);
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

    //?????? ?????????
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

    // ????????? ????????????
    private boolean isMeetingLike(Member member, Meeting meeting) {
        return heartMeetingRepository.existsByMemberAndMeeting(member, meeting);
    }

    //?????? ????????? ????????????
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

    //?????? ??????, ?????? ?????? ??????
    private void isValidateDate (MeetingRequestDto requestDto){
        if (!requestDto.getJoinEndDate().isAfter(requestDto.getJoinStartDate())){
            throw new CustomErrorException(ErrorCode.WRONG_JOIN_DATE);
        }if(!requestDto.getJoinEndDate().isBefore(requestDto.getMeetingStartDate())){
            throw new CustomErrorException(ErrorCode.WRONG_DATE);
        }if(!(requestDto.getMeetingEndDate().isAfter(requestDto.getMeetingStartDate())||requestDto.getMeetingEndDate().isEqual(requestDto.getMeetingStartDate()))){
            throw new CustomErrorException(ErrorCode.WRONG_MEETING_DATE);
        }
    }

    // ?????? ?????? ?????? ?????? ??????
    private void isValidatePeopleNumber (MeetingRequestDto requestDto){
        if(requestDto.getLimitPeople()  <= 1){
            throw new CustomErrorException(ErrorCode.WRONG_LIMIT_PEOPLE);
        }
    }

    //?????? ????????? ?????? ?????? ?????? ??????
    private void isValidateStatus (Meeting meeting){
        if(!(meeting.getMeetingStatus() == MeetingStatus.CAN_JOIN || meeting.getMeetingStatus() == MeetingStatus.READY_FOR_JOIN)){
            if(meeting.getNowPeople() > 1){
                throw new CustomErrorException(ErrorCode.CAN_NOT_UPDATE_MEETING);
            }throw new CustomErrorException(ErrorCode.CAN_NOT_UPDATE_MEETING2);
        }
    }
}
