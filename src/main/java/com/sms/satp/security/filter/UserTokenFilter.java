package com.sms.satp.security.filter;

import static com.sms.satp.security.TokenType.USER;

import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.utils.JwtUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;

    public UserTokenFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain chain) throws ServletException, IOException {
        // Get jwt token
        String token = JwtUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            chain.doFilter(request, response);
            return;
        }
        String tokenType = jwtTokenManager.getTokenType(token);
        if (!USER.name().equalsIgnoreCase(tokenType) || !jwtTokenManager.validate(token)) {
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = jwtTokenManager.createAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }
}