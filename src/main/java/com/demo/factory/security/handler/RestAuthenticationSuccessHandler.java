package com.demo.factory.security.handler;

import com.demo.factory.domain.Member;
import com.demo.factory.repository.member.MemberRepository;
import com.demo.factory.security.dto.MemberDtoForSecurity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component("restSuccessHandler")
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        ObjectMapper mapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();

        MemberDtoForSecurity principal = (MemberDtoForSecurity) authentication.getPrincipal();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        MemberDtoForSecurity memberDtoForSecurity;
        try {
            Member member = memberRepository.findByUserId(principal.getUsername()).orElse(new Member());
            memberDtoForSecurity = new MemberDtoForSecurity(member);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mapper.writeValue(response.getWriter(), memberDtoForSecurity);
        clearAuthenticationAttributes(request);
    }
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}