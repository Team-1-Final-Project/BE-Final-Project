package com.innovation.backend.repository;

import com.innovation.backend.entity.HeartMeeting;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartMeetingRepository extends JpaRepository<HeartMeeting, Long> {
    boolean existsByMemberAndMeeting(Member member, Meeting meeting);
    void deleteByMemberAndMeeting(Member member, Meeting meeting);
}
