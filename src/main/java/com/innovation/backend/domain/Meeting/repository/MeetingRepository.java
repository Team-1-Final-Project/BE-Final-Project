package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
  Page<Meeting> findAllByOrderByCreatedAtDesc(Pageable pageable);
  List<Meeting> findTop4ByOrderByCreatedAtDesc();

//  List<Meeting> findAll(Pageable pageable);

}
