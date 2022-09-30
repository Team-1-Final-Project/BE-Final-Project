package com.innovation.backend.dto.response;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.TagBoard;
import com.innovation.backend.entity.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    public GetAllBoardDto(Board board, int heartBoardNums, String boardImage){
        this.boardId = board.getId();
        this.profileImage = board.getMember().getProfileImage();
        this.writerName = board.getMember().getNickname();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImage = boardImage;
        this.heartBoardNums = heartBoardNums;
        super.createdAt = board.getCreatedAt();
//        this.tagBoard = tagBoard;
//        this.commentsNum = commentsNum;
    }
}
