package com.innovation.backend.domain.Review.repository;

import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Review.domain.Review;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
  Page<Review> findAllByOrderByCreatedAtDesc(Pageable pageable);

  List<Review> findByMember (Member member);
}
