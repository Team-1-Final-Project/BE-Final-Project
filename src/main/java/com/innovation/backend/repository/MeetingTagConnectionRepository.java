package com.innovation.backend.repository;

import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.MeetingTagConnection;
import com.innovation.backend.entity.TagMeeting;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MeetingTagConnectionRepository extends JpaRepository<MeetingTagConnection, Long> {


  Optional<MeetingTagConnection> findByTagMeeting (TagMeeting tagMeeting);

  MeetingTagConnection findByMeeting(Meeting meeting);

  @Query(nativeQuery = true, value = "select m.* from meeting_tag_connection as m where tag_id = :tagId")
  List<MeetingTagConnection> findByTagId(Long tagId);

}
