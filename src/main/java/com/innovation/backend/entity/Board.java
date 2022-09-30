package com.innovation.backend.entity;

import javax.persistence.*;

import com.innovation.backend.dto.request.BoardRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends Timestamped{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

//  @JoinColumn(name = "member_nickname", nullable = false)
//  private String writer;
//
//  @JoinColumn(name = "member_profile_image")
//  private String profileImage;

  @Column(nullable = false)
  private String title;

//  @ElementCollection
//  @CollectionTable(name = "boardImage_id")
////  @OneToMany(fetch = FetchType.LAZY)
//  private List<String> boardImage = new ArrayList<>();

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

//  @Column
//  private int heartBoardNums;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HeartBoard> heartBoardList;

//  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//  private List<TagBoard> tagBoardList;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList;


  public Board(BoardRequestDto boardRequestDto, Member member, String boardImage){
    this.title = boardRequestDto.getTitle();
    this.content = boardRequestDto.getContent();
    this.boardImage = boardImage;
    this.member = member;
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

//  public void heart(){
//    this.heartBoardNums += 1;
//  }
//
//  public void unheart(){
//    this.heartBoardNums -= 1;
//  }

}
