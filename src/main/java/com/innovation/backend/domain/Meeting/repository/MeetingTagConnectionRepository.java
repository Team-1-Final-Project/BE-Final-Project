package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.MeetingTagConnection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingTagConnectionRepository extends JpaRepository<MeetingTagConnection, Long> {

  @Query(nativeQuery = true, value = "select m.* from meeting_tag_connection as m where tag_id in (:tagId) limit :offset, :limit")
  List<MeetingTagConnection> findByTagId(List<Long> tagId, long offset, int limit);

  @Query(nativeQuery = true, value = "select count(*) from meeting_tag_connection as m where tag_id in (:tagId)")
  Long findByTagIdCount(List<Long> tagId);
}
