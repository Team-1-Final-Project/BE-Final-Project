package com.innovation.backend.repository;

import com.innovation.backend.entity.TagMeeting;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagMeetingRepository extends JpaRepository<TagMeeting, Long> {
  TagMeeting findByName (String tagName);

  Optional<TagMeeting> findById(Long tagId);
}
