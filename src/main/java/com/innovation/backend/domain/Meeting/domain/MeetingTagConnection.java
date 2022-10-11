package com.innovation.backend.domain.Meeting.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MeetingTagConnection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  @ManyToOne
  @JoinColumn(name = "tag_id")
  private TagMeeting tagMeeting;

  public MeetingTagConnection(Meeting meeting, TagMeeting tagMeeting) {
    this.meeting = meeting;
    this.tagMeeting = tagMeeting;
  }
}
