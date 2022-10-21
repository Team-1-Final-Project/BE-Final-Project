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
public class MainBoardResponseDto extends Timestamped {
    private Long boardId;
    private String profileImage;
    private String writerName;
    private String title;
    private String content;
    private String boardImage;
    private int heartBoardNums;
    private final List<TagBoardResponseDto> tagBoards = new ArrayList<>();
    private int commentNums;

    public MainBoardResponseDto(Board board, int commentNums) {
        this.boardId = board.getId();
        this.writerName = board.getMember().getNickname();
        this.profileImage = board.getMember().getProfileImage();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = board.getBoardImage();
        this.heartBoardNums = board.getHeartBoardNums();
        this.commentNums = commentNums;
        super.createdAt = board.getCreatedAt();


        for (BoardTagConnection boardTagConnection : board.getBoardTagConnectionList()) {
            TagBoard tagBoard = boardTagConnection.getTagBoard();
            TagBoardResponseDto tagBoardResponseDto = new TagBoardResponseDto(tagBoard);
            tagBoards.add(tagBoardResponseDto);
        }

    }


}

