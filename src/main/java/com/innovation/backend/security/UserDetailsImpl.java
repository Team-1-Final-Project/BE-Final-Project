package com.innovation.backend.security;

import com.innovation.backend.domain.Member.domain.Member;
import com.innovation.backend.global.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserDetailsImpl implements UserDetails {

    private Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_USER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() { return member.getEmail(); }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
