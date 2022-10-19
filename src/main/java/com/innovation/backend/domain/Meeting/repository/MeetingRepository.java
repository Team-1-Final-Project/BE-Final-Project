package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.Meeting;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
  Page<Meeting> findAllByOrderByCreatedAtDesc(Pageable pageable);
  List<Meeting> findTop4ByOrderByCreatedAtDesc();
  @Query(nativeQuery = true, value = "select distinct m.* from meeting as m inner join meeting_tag_connection as mtc on m.id = mtc.meeting_id where mtc.tag_id in (:tagId) limit :offset, :limit")
  List<Meeting> findByTagId(List<Long> tagId, long offset, int limit);
  @Query(nativeQuery = true, value = "select count(*) from meeting as m inner join meeting_tag_connection as mtc on m.id = mtc.meeting_id where mtc.tag_id in (:tagId)")
  Long findByTagIdCount(List<Long> tagId);
  Page<Meeting> findByTitleContainsIgnoreCase (String keyword,Pageable pageable);
}
