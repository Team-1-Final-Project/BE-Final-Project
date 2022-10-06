package com.innovation.backend.domain.Board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoardTagConnection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagBoard tagBoard;

    public BoardTagConnection(Board board, TagBoard tagBoard) {
        this.board = board;
        this.tagBoard = tagBoard;
    }

}
