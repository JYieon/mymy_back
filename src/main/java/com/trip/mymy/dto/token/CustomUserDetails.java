package com.trip.mymy.dto.token;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import com.trip.mymy.dto.MemberDTO;

public class CustomUserDetails implements UserDetails {
    private final MemberDTO member;

    public CustomUserDetails(MemberDTO member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getAuthorities();
    }

    @Override
    public String getPassword() {
        return member.getPwd(); // 비밀번호가 있다면 추가
    }

    @Override
    public String getUsername() {
        return member.getId();
    }

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

