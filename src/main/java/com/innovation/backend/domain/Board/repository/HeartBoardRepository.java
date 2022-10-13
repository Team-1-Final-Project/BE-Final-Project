package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.domain.HeartBoard;
import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HeartBoardRepository extends JpaRepository<HeartBoard,Long> {
    boolean existsByMemberAndBoard(Member member, Board board);
    void deleteByMemberAndBoard(Member member, Board board);
    List<HeartBoard> findByMember (Member member);
    void deleteByBoard (Board board);
    int countByBoard (Board board);

    int countByMember(Member member);
}