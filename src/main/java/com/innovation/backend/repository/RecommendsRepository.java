package com.innovation.backend.repository;

import com.innovation.backend.entity.Recommends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendsRepository extends JpaRepository<Recommends, Long> {
    Page<Recommends> findAllBy(Pageable pageable);
}
