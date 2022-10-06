package com.innovation.backend.domain.Member.repository;

import com.innovation.backend.domain.Member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmailAndProvider(String email, String provider);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long id);

}
