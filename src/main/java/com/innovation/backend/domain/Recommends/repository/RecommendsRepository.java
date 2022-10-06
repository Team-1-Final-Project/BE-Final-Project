package com.innovation.backend.domain.Recommends.repository;

import com.innovation.backend.domain.Recommends.domain.Recommends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendsRepository extends JpaRepository<Recommends, Long> {
    Page<Recommends> findAllBy(Pageable pageable);
}
