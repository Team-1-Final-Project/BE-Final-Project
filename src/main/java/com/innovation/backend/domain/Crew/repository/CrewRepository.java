package com.innovation.backend.domain.Crew.repository;

import com.innovation.backend.domain.Crew.domain.Crew;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
  Optional<Crew> findByMemberAndMeeting(Member member, Meeting meeting);
  List<Crew> findByMeeting (Meeting meeting);

  void deleteByMemberAndMeeting (Member member, Meeting meeting);

  List<Crew> findByMember (Member member);

}
