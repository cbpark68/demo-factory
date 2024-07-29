package com.demo.factory.security.service;

import com.demo.factory.domain.Member;
import com.demo.factory.domain.MemberAuth;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.security.dto.MemberContext;
import com.demo.factory.security.dto.MemberDtoForSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = userRepository.findByUserId(username).orElse(new Member());
        if (member.getUserName().equals("error")) {
          throw new UsernameNotFoundException("No user found with username: " + username);
        }
        MemberAuth findMemberAuth = member.getAuthList().get(0);
        List<GrantedAuthority> authorities =  new ArrayList<>();
        SimpleGrantedAuthority sim
                = new SimpleGrantedAuthority(String.valueOf(findMemberAuth.getAuth()));
        authorities.add(sim);
        MemberDtoForSecurity memberDtoForSecurity = new MemberDtoForSecurity(member);
        return new MemberContext(memberDtoForSecurity, authorities);
    }
}
