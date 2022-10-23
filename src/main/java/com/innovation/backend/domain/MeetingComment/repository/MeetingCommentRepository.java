package com.innovation.backend.domain.MeetingComment.repository;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.MeetingComment.domain.MeetingComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MeetingCommentRepository extends JpaRepository<MeetingComment, Long> {
    Optional<MeetingComment> findById(MeetingComment meetingComment);
    void deleteById(Long meetingCommentId);
    List<MeetingComment> findAllByMeetingOrderByIdDesc(Meeting meeting);

}
