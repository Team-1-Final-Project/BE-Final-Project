package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.Board;
import com.innovation.backend.domain.Board.dto.response.BoardResponseDto;
import com.innovation.backend.domain.Board.dto.response.GetAllBoardDto;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<Board> findAllByOrderByCreatedAtDesc();

    List<Board> findTop4ByCreatedAtBetweenOrderByHeartBoardNumsDesc(LocalDateTime start, LocalDateTime end);

    List<Board> findByMember(Member member);

    long countBoardById(Long id);
    int countByMember(Member member);
    Slice<Board> findAllByOrderByCreatedAtDesc (Pageable pageable);
    @Query(nativeQuery = true, value = "select distinct m.* from board as m inner join board_tag_connection as mtc on m.id = mtc.board_id where mtc.tag_id in (:tagId) ")
    List<Board> findByTagId(List<Long> tagId,Pageable pageable);
    @Query(nativeQuery = true, value = "select count(*) from board as m inner join board_tag_connection as mtc on m.id = mtc.board_id where mtc.tag_id in (:tagId)")
    Long findByTagIdCount(List<Long> tagId);
    Page<Board> findByTitleContainsIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

}

