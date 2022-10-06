package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.HeartMeeting;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartMeetingRepository extends JpaRepository<HeartMeeting, Long> {
    boolean existsByMemberAndMeeting(Member member, Meeting meeting);
    void deleteByMemberAndMeeting(Member member, Meeting meeting);
}
