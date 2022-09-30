package com.innovation.backend.service;

import com.innovation.backend.dto.request.CommentRequestDto;
import com.innovation.backend.dto.response.CommentResponseDto;
import com.innovation.backend.dto.response.ResponseDto;
import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.Comment;
import com.innovation.backend.entity.Member;
import com.innovation.backend.enums.ErrorCode;
import com.innovation.backend.jwt.UserDetailsImpl;
import com.innovation.backend.repository.BoardRepository;
import com.innovation.backend.repository.CommentRepository;
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
