package com.innovation.backend.repository;

import com.innovation.backend.entity.Board;
import com.innovation.backend.entity.HeartBoard;
import com.innovation.backend.entity.Meeting;
import com.innovation.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartBoardRepository extends JpaRepository<HeartBoard, Long> {
    List<HeartBoard> findAllByBoard (Board board);
    void deleteByBoard (Board board);
    int countByBoard (Board board);
}
