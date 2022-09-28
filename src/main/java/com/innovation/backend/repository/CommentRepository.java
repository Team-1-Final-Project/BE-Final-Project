package com.innovation.backend.repository;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> findAllByBoardOrderByCreatedAtDesc(Board board);
}
