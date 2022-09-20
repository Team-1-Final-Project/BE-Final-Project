package com.innovation.backend.repository;

import com.innovation.backend.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

}
