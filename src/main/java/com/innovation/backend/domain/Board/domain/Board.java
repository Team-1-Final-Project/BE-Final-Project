package com.innovation.backend.domain.Board.domain;

import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Board.dto.request.BoardRequestDto;
import com.innovation.backend.global.util.Timestamped;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column
  private String boardImage;

  /// 좋아요 부분
  @Column
  private int heartBoardNums = 0;

  @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
  private Set<HeartBoard> heartBoards = new HashSet<>();

  // 좋아요수 증가
  public void addBoardLike(int heartBoardNums){
    this.heartBoardNums = heartBoardNums;
  }

  //게시글 태그
  @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE,orphanRemoval = true)
  @JsonIgnore
  private Set<BoardTagConnection> boardTagConnectionList = new HashSet<>();


  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HeartBoard> heartBoardList;


  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList;


  public Board(BoardRequestDto boardRequestDto, Member member, String boardImage){
    this.title = boardRequestDto.getTitle();
    this.content = boardRequestDto.getContent();
    this.boardImage = boardImage;
    this.member = member;
  }

  public void setBoardTagConnectionList (Set<BoardTagConnection> boardTagConnections){
    this.boardTagConnectionList = boardTagConnections;
  }

  public void alter(BoardRequestDto boardRequestDto, String boardImageAlter){
    String title = boardRequestDto.getTitle();
    String content = boardRequestDto.getContent();
    String boardImage = boardImageAlter;
    if(title != null){
      this.title = title;
    }
    if(content != null){
      this.content = content;
    }
    if(boardImage != null){
      this.boardImage = boardImage;
    } // 수정되지 않은 데이터 기존 유지
  }

  public boolean validateMember(Long memberId) {
    Long thisMemberId = this.member.getId();
    return !memberId.equals(thisMemberId);
  }

}
