package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.Comment;
import com.innovation.backend.entity.TagBoard;
import com.innovation.backend.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BoardResponseDto extends Timestamped {
    private Long boardId;
    private String profileImage;
    private String writerName;
    private String title;
    private String content;
    private String boardImage;
    private int heartBoardNums;
//    private Long commentsNum;
    private List<TagBoard> tagBoard;
    private List<CommentResponseDto> commentResponseDtoList;


    public BoardResponseDto(Board board, int heartBoardNums){
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = heartBoardNums;
        super.createdAt = board.getCreatedAt();
        this.tagBoard =  board.getTagBoardList();
//        this.commentsNum = commentsNum;
    }
    public BoardResponseDto(Board board, int heartBoardNums, List<CommentResponseDto> commentResponseDtoList){
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = heartBoardNums;
        super.createdAt = board.getCreatedAt();
        this.tagBoard =  board.getTagBoardList();
        this.commentResponseDtoList = commentResponseDtoList;
//        this.commentsNum = commentsNum;
    }
}
