package com.innovation.backend.domain.Recommends.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Recommends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String address;
    private String content;
    private String type;
    private String image;

    // 오프라인샵 저장
    public Recommends(String title, String address, String type) {
        this.title = title;
        this.address = address;
        this.type = type;
    }
}