package com.innovation.backend.domain.Review.repository;

import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Review.domain.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
  List<Review> findAllByOrderByCreatedAtDesc();

  List<Review> findByMember (Member member);
}
