package com.innovation.backend.domain.Meeting.repository;

import com.innovation.backend.domain.Meeting.domain.MeetingTagConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingTagConnectionRepository extends JpaRepository<MeetingTagConnection, Long> {

//  @Query(nativeQuery = true, value = "select m.* from meeting_tag_connection as m where tag_id in (:tagId) limit :offset, :limit")
//  List<MeetingTagConnection> findByTagId(List<Long> tagId, long offset, int limit);

//  @Query(nativeQuery = true, value = "select count(*) from meeting_tag_connection as m where tag_id in (:tagId)")
//  Long findByTagIdCount(List<Long> tagId);
}
