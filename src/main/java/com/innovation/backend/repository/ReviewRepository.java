package com.innovation.backend.repository;

import com.innovation.backend.entity.Member;
import com.innovation.backend.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
  List<Review> findAllByOrderByCreatedAtDesc();

  List<Review> findByMember (Member member);
}
