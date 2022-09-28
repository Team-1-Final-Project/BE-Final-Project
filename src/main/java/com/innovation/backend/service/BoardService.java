package com.innovation.backend.service;

import com.innovation.backend.dto.request.BoardRequestDto;
import com.innovation.backend.dto.response.BoardResponseDto;
import com.innovation.backend.dto.response.CommentResponseDto;
import com.innovation.backend.dto.response.GetAllBoardDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.*;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.exception.CustomErrorException;
import com.innovation.backend.jwt.TokenProvider;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.BoardRepository;
import com.innovation.backend.repository.CommentRepository;
import com.innovation.backend.repository.HeartBoardRepository;
import com.innovation.backend.util.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@AllArgsConstructor
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsImpl userDetails;
    private final HeartBoardRepository heartBoardRepository;
    private final CommentRepository commentRepository;
    private final S3Upload s3Upload;

    //게시글 전체 최신순으로 정렬
    @Transactional
    public ResponseDto<?> getAllBoard(){
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<GetAllBoardDto> getAllBoardDtoList = new ArrayList<>();

        for(Board board : boardList){
            int heartBoardNums = heartBoardRepository.countByBoard(board);
            String boardImage = board.getBoardImage();
            if(!boardImage.isEmpty()){
                boardImage = board.getBoardImage();
            }
            GetAllBoardDto getAllBoardDto = new GetAllBoardDto(board, heartBoardNums, boardImage);
            getAllBoardDtoList.add(getAllBoardDto);
        }
        return ResponseDto.success(getAllBoardDtoList);
    }

    // 게시글 작성
    @Transactional
    public ResponseDto<?> createBoard(UserDetailsImpl userDetails, BoardRequestDto boardRequestDto, MultipartFile uploadImage) throws IOException {

//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
//        }
//        Member member = tokenProvider.getMemberFromAuthentication();
//        if (null == member) {
//            return ResponseDto.fail(ErrorCode.MEMBER_NOT_FOUND);
//        }

        Member member = userDetails.getMember(); //회원 검사

        String boardImage=null;

        if (uploadImage != null &&!uploadImage.isEmpty()) {
                boardImage = s3Upload.uploadFiles(uploadImage, "boardImages");
        }

        Board board = new Board(boardRequestDto, member, boardImage);
        boardRepository.save(board);
//        List<String> tagBoardList = boardRequestDto.getTagBoard();
        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums());
        return ResponseDto.success(boardResponseDto);

    }

    //게시글 상세 조회
    @Transactional
    public ResponseDto<?> getBoard(Long id) {
        Board board = isPresentBoard(id);
        if(null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }

//        List<TagBoard> tagBoardList = tagBoardRepository.findAllByBoard(board);
//        List<String> tagNameList = new ArrayList<>();
//
//        for(TagBoard tagBoard : tagBoardList) {
//            String tagName = tagBoard.getTagName();
//            tagNameList.add(tagName);
//        }
        List<Comment> commentList = commentRepository.findAllByBoardOrderByCreatedAtDesc(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for(Comment comment : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums(), commentResponseDtoList);
        return ResponseDto.success(boardResponseDto);
    }


    //게시글 수정
    @Transactional
    public ResponseDto<?> alterBoard(Long id, UserDetailsImpl userDetails, BoardRequestDto boardRequestDto, MultipartFile uploadImage) throws IOException {
        Board board = isPresentBoard(id);
        Board boardAlter = boardRepository.findBoardById(id);

        if (null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        Member member = userDetails.getMember();

        if(board.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        String boardImage = boardAlter.getBoardImage();

            if (boardImage != null &&!boardImage.isEmpty()) {
                    s3Upload.fileDelete(boardImage);
                    boardImage = s3Upload.uploadFiles(uploadImage, "boardImages");
            }
        board.alter(boardRequestDto, boardImage);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums());
        return ResponseDto.success(boardResponseDto);
    }

    //게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long id, UserDetailsImpl userDetails) {
        Board board = isPresentBoard(id);
        Board boardDelete = boardRepository.deleteBoardById(id);
        if (null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        Member member = userDetails.getMember();

        if(board.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        String boardImage = boardDelete.getBoardImage();

            if (boardImage != null &&!boardImage.isEmpty()) {
                s3Upload.fileDelete(boardImage);

            }
        boardRepository.delete(board);
        return ResponseDto.success("삭제가 성공적으로 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public Board isPresentBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        return optionalBoard.orElse(null);
    }

}

