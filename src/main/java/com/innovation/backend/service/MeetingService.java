package com.innovation.backend.service;

import com.innovation.backend.dto.request.MeetingRequestDto;
import com.innovation.backend.dto.request.TagMeetingRequestDto;
import com.innovation.backend.dto.response.MeetingLikeResponseDto;
import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.entity.*;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.*;
import com.innovation.backend.util.S3Upload;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final EntityManager entityManager;

    //모임 생성
    @Transactional
    public MeetingResponseDto createMeeting(MeetingRequestDto requestDto, Member member,
                                            MultipartFile image) {

        String meetingImage = null;

        if (image != null && !image.isEmpty()) {
            try {
                meetingImage = s3Upload.uploadFiles(image, "images");
                log.info(meetingImage);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        Meeting meeting = new Meeting(requestDto, member, meetingImage); // 모임 객체 생성
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
    public void updateMeeting(Long meetingId, MeetingRequestDto requestDto, Member member) {
        //해당 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        //모임장과 같은 유저인지 확인하기
        if (meeting.isWrittenBy(member)) {

            // 태그, 이미지 외 수정
            meeting.update(requestDto);

            // 기존 태그와 비교
            Set<MeetingTagConnection> oldMeetingTagConnectionList = meeting.getMeetingTagConnectionList();

            // 새로운 tag id 리스트
            List<Long> newTagMeetingIdList = requestDto.getTagMeetingIds();

            // 기존 태그 id list
            List<Long> oldMeetingTagConnectionIdList = new ArrayList<>();
            for (MeetingTagConnection meetingTagConnection : oldMeetingTagConnectionList) {
                oldMeetingTagConnectionIdList.add(meetingTagConnection.getId());
            }

            for (MeetingTagConnection oldMeetingTagConnection : oldMeetingTagConnectionList) {
                for (Long newMeetingTagId : newTagMeetingIdList) {
                    if (oldMeetingTagConnection.getTagMeeting().getId().equals(newMeetingTagId)) {
                        // newMeetingTagIdList에 남아 있는 건 추가할 태그
                        newTagMeetingIdList.remove(newMeetingTagId);

                        // oldMeetingTagIdList에 남아 있는 건 삭제할 태그
                        oldMeetingTagConnectionIdList.remove(oldMeetingTagConnection.getId());
                    }
                }
            }

            // 삭제
            for (Long oldMeetingTagId : oldMeetingTagConnectionIdList) {
                MeetingTagConnection deleteMeetingTagConnection = findTagId(oldMeetingTagConnectionList, oldMeetingTagId);
                if (deleteMeetingTagConnection != null) {
                    oldMeetingTagConnectionList.remove(deleteMeetingTagConnection);
                    meetingTagConnectionRepository.delete(deleteMeetingTagConnection);
                }
            }

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

        } else {
            throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
        }
    }

    private MeetingTagConnection findTagId(
            Set<MeetingTagConnection> meetingTagConnectionList, Long meetingTagConnectionId) {
        for (MeetingTagConnection meetingTagConnection : meetingTagConnectionList) {
            if (meetingTagConnection.getId().equals(meetingTagConnectionId)) {
                return meetingTagConnection;
            }
        }

        return null;
    }


    //모임 이미지 수정
    @Transactional
    public void updateMeetingImage(Long meetingId, Member member, MultipartFile image) {
        //해당 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

        String meetingImage = meeting.getMeetingImage();
        //모임장과 같은 유저인지 확인하기
        if (meeting.isWrittenBy(member)) {
            if (meetingImage != null) {
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
                if (!image.isEmpty()) {
                    try {
                        meetingImage = s3Upload.uploadFiles(image, "images");
                        System.out.println(meetingImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new CustomErrorException(ErrorCode.NOT_ADMIN_OF_MEETING);
        }
        meeting.updateMeetingImage(meetingImage);
        meetingRepository.save(meeting);
    }


    //모임 삭제
    @Transactional
    public void deleteMeeting(Long meetingId, Member member) {
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

    //모임 이미지 삭제
    @Transactional
    public void deleteImage(Long meetingId, Member member) {
        //해당 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new CustomErrorException(ErrorCode.NOT_FOUND_MEETING));

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
//    StringBuilder selectQuery = new StringBuilder();
//    selectQuery.append(
//        "select m.* from meeting as m left join meeting_tag_connection as mtc on mtc.meeting_id = m.id");
//
//    if (tagMeetingRequestDto.getTagIds().size() > 0) {
//      selectQuery.append(" where ");
//      for (int i = 0; i < tagMeetingRequestDto.getTagIds().size(); i++) {
//        if (i != 0) {
//          selectQuery.append(" or ");
//        }
//        selectQuery.append("mtc.tag_id = :id_" + i);
//      }
//    }
//
//    Query query = entityManager.createNativeQuery(selectQuery.toString());
//    if (tagMeetingRequestDto.getTagIds().size() > 0) {
//      for (int i = 0; i < tagMeetingRequestDto.getTagIds().size(); i++) {
//        query.setParameter("id_"+i , tagMeetingRequestDto.getTagIds().get(i));
//      }
//    }
//
//    List<Meeting> meetings = query.unwrap(NativeQuery.class).setResultTransformer(
//        Transformers.aliasToBean((Meeting.class))).getResultList();
//
//    return meetings.stream().map(MeetingResponseDto::new).collect(Collectors.toList());

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

    //참여한 모임만 조회 (사용자)


    //좋아요한 모임만 조회 (사용자)

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
    public void addMeetingLike(UserDetailsImpl userDetails, Long meetingId) {
        String userId = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userId).orElseThrow();
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
        Long likeNums = meeting.getHeartNums();
        HeartMeeting heartMeeting = new HeartMeeting(member, meeting);
        if (isMeetingLike(member, meeting)) {
            heartMeetingRepository.deleteByMemberAndMeeting(member, meeting);
            meeting.addMeetingLike(likeNums - 1);
        } else {
            heartMeetingRepository.save(heartMeeting);
            meeting.addMeetingLike(likeNums + 1);
        }
    }

    // 좋아요 중복방지
    private boolean isMeetingLike(Member member, Meeting meeting) {
        return heartMeetingRepository.existsByMemberAndMeeting(member, meeting);
    }

    //모임 좋아요 여부확인
    @Transactional
    public MeetingLikeResponseDto getMeetingLike(UserDetailsImpl userDetails, Long meetingId) {
        String userId = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userId).orElseThrow();
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow();
        boolean MeetingLike = isMeetingLike(member,meeting);
        return new MeetingLikeResponseDto(MeetingLike);
    }
}
