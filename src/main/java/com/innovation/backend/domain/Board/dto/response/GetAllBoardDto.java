package com.innovation.backend.domain.Board.dto.response;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.BoardTagConnection;
import com.innovation.backend.domain.Board.domain.TagBoard;
import com.innovation.backend.global.util.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetAllBoardDto extends Timestamped {
    private Long boardId;
    private String profileImage;
    private String writerName;
    private String boardImage;
    private String title;
    private String content;
    private int heartBoardNums;
//    private Long commentsNum;
//    private List<String> tagBoard;
    private int commentNums;
    private final List<TagBoardResponseDto> tagBoards = new ArrayList<>();


    public GetAllBoardDto(Board board, int heartBoardNums, int commentNums, String boardImage){
        this.boardId = board.getId();
        this.profileImage = board.getMember().getProfileImage();
        this.writerName = board.getMember().getNickname();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = boardImage;
        this.heartBoardNums = heartBoardNums;
        this.commentNums = commentNums;
        super.createdAt = board.getCreatedAt();
//        this.tagBoard = tagBoard;
        for (BoardTagConnection boardTagConnection : board.getBoardTagConnectionList()) {
            TagBoard tagBoard = boardTagConnection.getTagBoard();
            TagBoardResponseDto tagBoardResponseDto = new TagBoardResponseDto(tagBoard);
            tagBoards.add(tagBoardResponseDto);
        }
    }
}
