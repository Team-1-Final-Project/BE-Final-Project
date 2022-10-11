package com.innovation.backend.repository;

import com.innovation.backend.entity.Meeting;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
  List<Meeting> findAllByOrderByCreatedAtDesc();
  List<Meeting> findTop4ByOrderByCreatedAtDesc();

//  List<Meeting> findAll(Pageable pageable);

}
