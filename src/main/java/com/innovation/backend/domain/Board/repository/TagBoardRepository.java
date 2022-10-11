package com.innovation.backend.domain.Board.repository;

import com.innovation.backend.domain.Board.domain.TagBoard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagBoardRepository extends JpaRepository<TagBoard, Long> {

    TagBoard findByTagName(String tagName);
    Optional<TagBoard> findById(Long tagId);
}
