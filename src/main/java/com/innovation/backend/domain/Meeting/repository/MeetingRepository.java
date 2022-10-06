package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
  List<Meeting> findAllByOrderByCreatedAtDesc();
  List<Meeting> findTop4ByOrderByCreatedAtDesc();

//  List<Meeting> findAll(Pageable pageable);

}
