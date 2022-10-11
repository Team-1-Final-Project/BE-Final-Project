package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.BoardTagConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardTagConnectionRepository extends JpaRepository<BoardTagConnection,Long> {

    @Query(nativeQuery = true, value = "select m.* from board_tag_connection as m where tag_id = :tagId")
    List<BoardTagConnection> findByTagId(Long tagId);
}
