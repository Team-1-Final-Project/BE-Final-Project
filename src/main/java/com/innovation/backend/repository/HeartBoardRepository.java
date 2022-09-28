package com.innovation.backend.repository;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.HeartBoard;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartBoardRepository extends JpaRepository<HeartBoard,Long> {
    boolean existsByMemberAndBoard(Member member, Board board);
    void deleteByMemberAndBoard(Member member, Board board);
}

