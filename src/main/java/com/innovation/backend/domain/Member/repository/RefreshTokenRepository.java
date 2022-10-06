package com.innovation.backend.domain.Member.repository;

import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.domain.Member.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}