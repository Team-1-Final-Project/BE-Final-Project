package com.innovation.backend.domain.Meeting.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TagMeeting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;


  @OneToMany(mappedBy = "tagMeeting")
  @JsonIgnore
  private List<MeetingTagConnection> meetingTagConnectionList = new ArrayList<>();

  public TagMeeting(MeetingTagConnection meetingTagConnection){
    this.id = meetingTagConnection.getId();
  }

}
