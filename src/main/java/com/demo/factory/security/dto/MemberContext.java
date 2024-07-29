package com.demo.factory.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class MemberContext implements UserDetails {
    private MemberDtoForSecurity memberDto;
    private final List<GrantedAuthority> authList;

    public MemberContext(MemberDtoForSecurity memberDto, List<GrantedAuthority> authList) {
      this.memberDto = memberDto;
      this.authList = authList;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authList;
    }
    @Override
    public String getPassword() {
        return memberDto.getPassword();
    }
    @Override
    public String getUsername() {
        return memberDto.getUsername();
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
