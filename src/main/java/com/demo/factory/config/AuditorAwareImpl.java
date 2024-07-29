package com.demo.factory.config;

import com.demo.factory.security.dto.MemberDtoForSecurity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "anonymous";
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        } catch (ClassCastException e) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof MemberDtoForSecurity) {
                username = ((MemberDtoForSecurity)authentication.getPrincipal()).getUsername();
            }
        } catch (Exception e) {
            try {
                username = (String) authentication.getPrincipal();
            } catch (Exception exception) {
                username = "anonymous";
            }
        }
        return Optional.of(username);
    }

}