package com.innovation.backend.domain.Board.service;

import com.innovation.backend.domain.Badge.service.BadgeService;
import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.BoardTagConnection;
import com.innovation.backend.domain.Board.domain.HeartBoard;
import com.innovation.backend.domain.Board.domain.TagBoard;
import com.innovation.backend.domain.Board.dto.request.BoardRequestDto;
import com.innovation.backend.domain.Board.dto.request.TagBoardRequestDto;
import com.innovation.backend.domain.Board.dto.response.BoardLikeResponseDto;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.GetAllBoardDto;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.domain.Board.repository.BoardTagConnectionRepository;
import com.innovation.backend.domain.Board.repository.HeartBoardRepository;
import com.innovation.backend.domain.Board.repository.TagBoardRepository;
import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.domain.Comment.dto.response.CommentResponseDto;
import com.innovation.backend.domain.Comment.repository.CommentRepository;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Meeting.dto.request.TagMeetingRequestDto;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.repository.MemberRepository;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.global.exception.CustomErrorException;
import com.innovation.backend.global.util.S3Upload;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.security.jwt.TokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final HeartBoardRepository heartBoardRepository;
    private final TokenProvider tokenProvider;
    private final UserDetailsImpl userDetails;
    private final CommentRepository commentRepository;
    private final S3Upload s3Upload;
    private final TagBoardRepository tagBoardRepository;
    private final BoardTagConnectionRepository boardTagConnectionRepository;
    private final BadgeService badgeService;

    //게시글 좋아요
    @Transactional
    public BoardLikeResponseDto addBoardLike(UserDetailsImpl userDetails, Long boardId) {
        String userId = userDetails.getUsername();
        Member member = memberRepository.findByEmail(userId).orElseThrow();
        Board board = boardRepository.findById(boardId).orElseThrow();
        int likeNums = board.getHeartBoardNums();
        boolean boardLike = isBoardLike(member, board);
        HeartBoard heartBoard = new HeartBoard(member, board);
        if (isBoardLike(member, board)) {
            heartBoardRepository.deleteByMemberAndBoard(member, board);
            board.addBoardLike(likeNums - 1);
            return new BoardLikeResponseDto(!boardLike);
        } else {
            heartBoardRepository.save(heartBoard);
            board.addBoardLike(likeNums + 1);
            badgeService.getHeartMakerBadge(userDetails, "HeartMaker Badge");
            return new BoardLikeResponseDto(!boardLike);
        }
    }

    //좋아요 중복방지
    private boolean isBoardLike(Member member, Board board) {
        return heartBoardRepository.existsByMemberAndBoard(member, board);
    }

    //게시글 좋아요 확인
    @Transactional
    public BoardLikeResponseDto getBoardLike(UserDetailsImpl userDetails, Long boardId) {
        boolean boardLike = false;
        if (userDetails != null) {
            String userId = userDetails.getUsername();
            Member member = memberRepository.findByEmail(userId).orElseThrow();
            Board board = boardRepository.findById(boardId).orElseThrow();
            boardLike = isBoardLike(member, board);
        }
        return new BoardLikeResponseDto(boardLike);
    }

    //게시글 전체 최신순으로 정렬

    @Transactional
    public ResponseDto<Slice<GetAllBoardDto>> getAllBoard(Pageable pageable) {
        Slice<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc(pageable);
        {
            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        }
        List<GetAllBoardDto> getAllBoardDtoList = new ArrayList<>();

        for (Board board : boardList) {
            int heartBoardNums = heartBoardRepository.countByBoard(board);
            int commentNums = commentRepository.countCommentsByBoard(board);
            Board boardById = boardRepository.findBoardById(board.getId());
            String boardImage = boardById.getBoardImage();

            GetAllBoardDto getAllBoardDto = new GetAllBoardDto(board, heartBoardNums, commentNums, boardImage);
            getAllBoardDtoList.add(getAllBoardDto);
        }
        return ResponseDto.success(new SliceImpl<>(getAllBoardDtoList, pageable, boardList.isLast()));
    }


    // 게시글 작성
    @Transactional
    public ResponseDto<?> createBoard(UserDetailsImpl userDetails, BoardRequestDto boardRequestDto, MultipartFile uploadImage) throws IOException {

        Member member = userDetails.getMember(); //회원 검사

        String boardImage = null;

        if (uploadImage != null && !uploadImage.isEmpty()) {
            boardImage = s3Upload.uploadFiles(uploadImage, "boardImages");
        } else if (uploadImage == null || uploadImage.isEmpty()) {
            boardImage = null;
        }

        Board board = new Board(boardRequestDto, member, boardImage);
        addBoardTagConnection(boardRequestDto, board);

        boardRepository.save(board);

        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums());
        badgeService.getWelcomeCommunityBadge(userDetails, "Welcome Community Badge");
        return ResponseDto.success(boardResponseDto);

    }

    //게시글 상세 조회
    @Transactional
    public ResponseDto<?> getBoard(Long id) {
        Board board = isPresentBoard(id);
        if (null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        int commentNums = commentRepository.countCommentsByBoard(board);

        List<Comment> commentList = commentRepository.findAllByBoardOrderByCreatedAtDesc(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums(), commentNums, commentResponseDtoList);
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

        if (board.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        int commentNums = commentRepository.countCommentsByBoard(board);

        List<Comment> commentList = commentRepository.findAllByBoardOrderByCreatedAtDesc(board);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }

        String boardImage = boardAlter.getBoardImage();

        if(uploadImage == null) {
            boardImage = board.getBoardImage();
        } else if(!uploadImage.isEmpty()) {
            if(boardImage != null) {
                s3Upload.fileDelete(boardImage);
            } else
            boardImage = s3Upload.uploadFiles(uploadImage, "boardImages");
        }

        board.alter(boardRequestDto, boardImage);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board, board.getHeartBoardNums(), commentNums, commentResponseDtoList);
        return ResponseDto.success(boardResponseDto);
    }

    //게시글 삭제
    @Transactional
    public ResponseDto<?> deleteBoard(Long id, UserDetailsImpl userDetails) {
        Board board = isPresentBoard(id);

        if (null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        Member member = userDetails.getMember();

        if (board.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }

        String boardImage = board.getBoardImage();

        if (boardImage != null && !boardImage.isEmpty()) {
            s3Upload.fileDelete(boardImage);
        }

        boardRepository.delete(board);
        return ResponseDto.success("게시글 삭제가 성공적으로 완료되었습니다.");
    }

    @Transactional(readOnly = true)
    public Board isPresentBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        return optionalBoard.orElse(null);
    }

    private void addBoardTagConnection(BoardRequestDto boardrequestDto, Board board) {
        Set<BoardTagConnection> boardTagConnectionList = new HashSet<>();
        for (Long tagId : boardrequestDto.getTagBoardIds()) {
            TagBoard tagBoard = tagBoardRepository.findById(tagId)
                    .orElseThrow(NullPointerException::new);
            BoardTagConnection boardTagConnection = new BoardTagConnection(board, tagBoard);
            boardTagConnection = boardTagConnectionRepository.save(boardTagConnection);
            boardTagConnectionList.add(boardTagConnection);
        }
        board.setBoardTagConnectionList(boardTagConnectionList);
    }

    public Page<BoardResponseDto> getBoardByTag(TagBoardRequestDto tagBoardRequestDto, Pageable pageable) {

        Long totalElement = boardRepository.findByTagIdCount(tagBoardRequestDto.getTagIds());
        List<Board> boardList = boardRepository.findByTagId(tagBoardRequestDto.getTagIds(), pageable);
        List<BoardResponseDto> boardResponseDtoList = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDtoList.add(new BoardResponseDto(board));
        }

        return new PageImpl<>(boardResponseDtoList, pageable, totalElement);
    }


}
