package com.innovation.backend.repository;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.HeartBoard;
import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HeartBoardRepository extends JpaRepository<HeartBoard,Long> {
    boolean existsByMemberAndBoard(Member member, Board board);
    void deleteByMemberAndBoard(Member member, Board board);
    List<HeartBoard> findAllByBoard (Board board);
    void deleteByBoard (Board board);
    int countByBoard (Board board);
}