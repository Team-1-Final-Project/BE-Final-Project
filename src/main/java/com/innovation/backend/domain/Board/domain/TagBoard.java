package com.innovation.backend.domain.Board.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TagBoard {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String tagName;

  @OneToMany(mappedBy = "tagBoard")
  @JsonIgnore
  private List<BoardTagConnection> boardTagConnectionList = new ArrayList<>();

  public TagBoard(BoardTagConnection boardTagConnection) {
    this.id = boardTagConnection.getId();
  }

}