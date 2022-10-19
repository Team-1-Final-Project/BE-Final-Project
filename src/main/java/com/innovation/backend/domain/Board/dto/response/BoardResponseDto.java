package com.innovation.backend.domain.Board.dto.response;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.BoardTagConnection;
import com.innovation.backend.domain.Board.domain.TagBoard;
import com.innovation.backend.domain.Comment.dto.response.CommentResponseDto;
import com.innovation.backend.global.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private final List<TagBoardResponseDto> tagBoards = new ArrayList<>();
    private int commentNums;
    private List<CommentResponseDto> commentResponseDtoList;



    public BoardResponseDto(Board board, int heartBoardNums) {
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = heartBoardNums;
        super.createdAt = board.getCreatedAt();


        for (BoardTagConnection boardTagConnection : board.getBoardTagConnectionList()) {
            TagBoard tagBoard = boardTagConnection.getTagBoard();
            TagBoardResponseDto tagBoardResponseDto = new TagBoardResponseDto(tagBoard);
            tagBoards.add(tagBoardResponseDto);
        }
//        this.tagBoard =  board.getTagBoardList();
//        this.commentsNum = commentsNum;
    }

    public BoardResponseDto(Board board, int heartBoardNums, int commentNums, List<CommentResponseDto> commentResponseDtoList) {
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = heartBoardNums;
        super.createdAt = board.getCreatedAt();
        super.modifiedAt = board.getModifiedAt();
//        this.tagBoard =  board.getTagBoardList();
        for (BoardTagConnection boardTagConnection : board.getBoardTagConnectionList()) {
            TagBoard tagBoard = boardTagConnection.getTagBoard();
            TagBoardResponseDto tagBoardResponseDto = new TagBoardResponseDto(tagBoard);
            tagBoards.add(tagBoardResponseDto);
        }
        this.commentNums = commentNums;
        this.commentResponseDtoList = commentResponseDtoList;

    }

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = board.getHeartBoardNums();
        this.createdAt = board.getCreatedAt();
        for (BoardTagConnection boardTagConnection : board.getBoardTagConnectionList()) {
            TagBoard tagBoard = boardTagConnection.getTagBoard();
            TagBoardResponseDto tagBoardResponseDto = new TagBoardResponseDto(tagBoard);
            tagBoards.add(tagBoardResponseDto);
        }
    }
}

