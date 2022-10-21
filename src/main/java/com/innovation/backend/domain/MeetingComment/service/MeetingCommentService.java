package com.innovation.backend.domain.MeetingComment.service;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Crew.repository.CrewRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.repository.MeetingRepository;
import com.innovation.backend.domain.MeetingComment.domain.MeetingComment;
import com.innovation.backend.domain.MeetingComment.dto.request.MeetingCommentRequestDto;
import com.innovation.backend.domain.MeetingComment.dto.response.MeetingCommentResponseDto;
import com.innovation.backend.domain.MeetingComment.repository.MeetingCommentRepository;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeetingCommentService {
    private final MeetingCommentRepository meetingCommentRepository;
    private final MeetingRepository meetingRepository;
    private final CrewRepository crewRepository;
    private final MeetingComment meetingComment;

    @Transactional
    public ResponseDto<MeetingCommentResponseDto> createComment(Long meetingId, UserDetailsImpl userDetails, MeetingCommentRequestDto meetingCommentRequestDto) {
        Member member = userDetails.getMember();
        Meeting meeting = isPresentMeeting(meetingId);

        if(member == null) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        } else if ((meeting == null) || (meetingComment.validateMeeting(meeting.getId()) == false)) {
            return ResponseDto.fail(ErrorCode.NOT_FOUND_MEETING);
        }

        isCrewCheck(member,meeting);

        MeetingComment meetingComment = new MeetingComment(meeting, meetingCommentRequestDto, member);
        meetingCommentRepository.save(meetingComment);

        MeetingCommentResponseDto meetingCommentResponseDto = new MeetingCommentResponseDto(meetingComment);
        return ResponseDto.success(meetingCommentResponseDto);
    }

    public ResponseDto<List<MeetingCommentResponseDto>> getAllComments(Long meetingId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        Meeting meeting = isPresentMeeting(meetingId);

        if(member == null)  {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        } else if (meeting == null) {
            return ResponseDto.fail(ErrorCode.NOT_FOUND_MEETING);
        } else if (meetingComment.validateMeeting(meeting.getId()) == false) {
            return ResponseDto.fail(ErrorCode.NEVER_JOIN);
        }

        isCrewCheck(member,meeting);

        List<MeetingComment> meetingCommentList = meetingCommentRepository.findAllByMeetingOrderByIdDesc(meeting);
        List<MeetingCommentResponseDto> meetingCommentResponseDtoList = new ArrayList<>();

        for (MeetingComment meetingComment : meetingCommentList) {
            MeetingCommentResponseDto meetingCommentResponseDto = new MeetingCommentResponseDto(meetingComment);
            meetingCommentResponseDtoList.add(meetingCommentResponseDto);
        }
        return ResponseDto.success(meetingCommentResponseDtoList);
    }

    public ResponseDto<MeetingCommentResponseDto> alterComment(Long meetingCommentId, UserDetailsImpl userDetails, MeetingCommentRequestDto meetingCommentRequestDto) {
        Member member = userDetails.getMember();
        MeetingComment meetingComment = isPresentMeetingComment(meetingCommentId);

        if(member == null) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        } else if(meetingComment.validateMember(member.getId()) == false) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        if(meetingComment == null) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        } else if (meetingComment != isPresentMeetingComment(meetingCommentId)) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }
        meetingComment.alter(meetingCommentRequestDto);
        MeetingCommentResponseDto meetingCommentResponseDto = new MeetingCommentResponseDto(meetingComment);
        return ResponseDto.success(meetingCommentResponseDto);
    }

    public ResponseDto<String> deleteComment(Long meetingCommentId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        MeetingComment meetingComment = isPresentMeetingComment(meetingCommentId);

        if(member == null) {
            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
        } else if(meetingComment.validateMember(member.getId()) == false) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        if(meetingComment == null) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        } else if (meetingComment != isPresentMeetingComment(meetingCommentId)) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }
        meetingCommentRepository.delete(meetingComment);
        return ResponseDto.success("모임 내 해당 댓글을 삭제하였습니다.");
    }

    @Transactional(readOnly = true)
    public Meeting isPresentMeeting(Long meetingId) {
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        return optionalMeeting.orElse(null);
    }

    @Transactional(readOnly = true)
    public MeetingComment isPresentMeetingComment(Long meetingCommentId) {
        Optional<MeetingComment> optionalMeetingComment = meetingCommentRepository.findById(meetingCommentId);
        return optionalMeetingComment.orElse(null);
    }

    public void isCrewCheck (Member member, Meeting meeting) {
        Optional<Crew> crewOptional = crewRepository.findByMemberAndMeeting(member, meeting);
        if (crewOptional.isEmpty()) {
            throw new CustomErrorException(ErrorCode.NEVER_JOIN);
        }
    }

}
