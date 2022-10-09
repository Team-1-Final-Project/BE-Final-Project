package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    List<Board> findByMember(Member member);

    Slice<Board> findByIdLessThanAndOrderByIdDesc(Long id);
    Page<Board> findByIdLessThanAndIdEqualsOrderByDesc(Long id);
    long countBoardById(Long id);

    Slice<Board> findByIdLessThanAndOrderByIdDesc(Long id, Pageable pageable);
}

