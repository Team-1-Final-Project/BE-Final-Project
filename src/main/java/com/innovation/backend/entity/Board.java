package com.innovation.backend.entity;

import javax.persistence.*;

import com.innovation.backend.dto.request.BoardRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

  @Column
  private int heartBoardNums;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HeartBoard> heartBoardList;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TagBoard> tagBoardList;

  @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> commentList;

  public Board(BoardRequestDto boardRequestDto, Member member){
    this.title = boardRequestDto.getTitle();
    this.content = boardRequestDto.getContent();
    this.boardImage = boardRequestDto.getBoardImage();
    this.member = member;
  }

  public void alter(BoardRequestDto boardRequestDto){
    String title = boardRequestDto.getTitle();
    String content = boardRequestDto.getContent();
    String boardImage = boardRequestDto.getBoardImage();
    if(title != null){
      this.title = title;
    }
    if(content != null){
      this.content = content;
    }
    if(boardImage != null){
      this.boardImage = boardImage;
    }
  }

  public boolean validateMember(Long memberId) {
    Long thisMemberId = this.member.getId();
    return !memberId.equals(thisMemberId);
  }

  public void heart(){
    this.heartBoardNums += 1;
  }

  public void unheart(){
    this.heartBoardNums -= 1;
  }

}
