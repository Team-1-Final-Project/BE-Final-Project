package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.BoardTagConnection;
import com.innovation.backend.domain.Meeting.domain.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardTagConnectionRepository extends JpaRepository<BoardTagConnection, Long> {

}
