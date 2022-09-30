package com.innovation.backend.repository;

import com.innovation.backend.entity.Crew;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewRepository extends JpaRepository<Crew, Long> {
  Optional<Crew> findByMemberAndMeeting(Member member, Meeting meeting);
  List<Crew> findByMeeting (Meeting meeting);

  void deleteByMemberAndMeeting (Member member, Meeting meeting);

  List<Crew> findByMember (Member member);

}
