package com.innovation.backend.domain.Meeting.domain;

import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.util.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HeartMeeting extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "meeting_id")
  private Meeting meeting;

  public HeartMeeting(Member member, Meeting meeting) {
    this.member = member;
    this.meeting = meeting;

  }
}
