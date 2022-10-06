package com.innovation.backend.domain.Comment.service;

import com.innovation.backend.domain.Comment.dto.request.CommentRequestDto;
import com.innovation.backend.domain.Comment.dto.response.CommentResponseDto;
import com.innovation.backend.domain.Board.repository.BoardRepository;
import com.innovation.backend.global.common.response.ResponseDto;
import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.ErrorCode;
import com.innovation.backend.security.UserDetailsImpl;
import com.innovation.backend.domain.Comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ResponseDto<?> createComment(UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        Member member = userDetails.getMember();
        Board board = isPresentBoard(commentRequestDto.getBoardId());
        if (null == board) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }

        Comment comment = new Comment(board, commentRequestDto, member);
        commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return ResponseDto.success(commentResponseDto);
    }

    @Transactional
    public ResponseDto<?> alterComment(Long commentId, UserDetailsImpl userDetails, CommentRequestDto commentRequestDto) {
        Comment comment = isPresentComment(commentId);
//        Comment commentAlter = commentRepository.findCommentById(commentId);

        if (null == comment) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        Member member = userDetails.getMember();

        if(comment.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }
        comment.alter(commentRequestDto);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return ResponseDto.success(commentResponseDto);

    }

    @Transactional
    public ResponseDto<?> deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = isPresentComment(commentId);

        if (null == comment) {
            return ResponseDto.fail(ErrorCode.ENTITY_NOT_FOUND);
        }
        Member member = userDetails.getMember();

        if (comment.validateMember(member.getId())) {
            return ResponseDto.fail(ErrorCode.NOT_SAME_MEMBER);
        }
        commentRepository.delete(comment);
        return ResponseDto.success("댓글을 삭제하였습니다.");
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElse(null);
    }

    @Transactional(readOnly = true)
    public Board isPresentBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        return optionalBoard.orElse(null);
    }
}
