package com.innovation.backend.repository;

import com.innovation.backend.dto.response.MeetingResponseDto;
import com.innovation.backend.entity.Meeting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
  List<Meeting> findAllByOrderByCreatedAtDesc();


}