package com.innovation.backend.repository;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {

    Optional<Board> findById(Long id);
    Board findBoardById(Long id);
    Board deleteBoardById(Long id);
    Optional<Board> findAllByMember(Member member);
    List<Board> findAllByOrderByHeartBoardNumsDesc();

//    List<Board> findAllByTagBoard();
    List<Board> findAllByOrderByCreatedAtDesc();
    List<Board> findTop4ByCreatedAtBetweenOrderByHeartBoardNumsDesc(LocalDateTime start, LocalDateTime end);
}