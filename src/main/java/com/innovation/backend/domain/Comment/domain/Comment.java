package com.innovation.backend.domain.Comment.domain;

import com.innovation.backend.domain.Comment.dto.request.CommentRequestDto;
import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.util.Timestamped;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Column(nullable = false)
  private String content;

  @JoinColumn(name = "board_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;

  public Comment(Board board, CommentRequestDto commentRequestDto, Member member) {
    this.board = board;
    this.content = commentRequestDto.getContent();
    this.member = member;
  }
  public void alter(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  public boolean validateMember(Long memberId) {
    Long thisMemberId = this.member.getId();
    return !memberId.equals(thisMemberId);
  }

}

