package com.innovation.backend.domain.Comment.repository;

import com.innovation.backend.domain.Comment.domain.Comment;
import com.innovation.backend.domain.Board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByBoardOrderByCreatedAtDesc(Board board);
    Optional<Comment> findById(Comment comment);
    Comment findCommentById(Long commentId);
    void deleteById(Long commentId);
    Optional<Comment> findAllByMember(Comment comment);
    List<Comment> findAllByOrderByCreatedAtDesc();

    int countCommentsByBoard(Board board);
}
