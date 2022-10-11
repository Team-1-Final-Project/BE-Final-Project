package com.innovation.backend.security;

import com.innovation.backend.entity.Member;
import com.innovation.backend.repository.MemberRepository;
import com.innovation.backend.security.UserDetailsImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member
                .map(UserDetailsImpl::new)
                .orElseThrow( () -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.") );
    }
}
