package com.sms.courier.security.filter;

import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.utils.JwtUtils;
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

public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;

    public JwtFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain chain) throws ServletException, IOException {
        // Get authorization header and validate
        String token = JwtUtils.getToken(request);
        if (StringUtils.isBlank(token) || !jwtTokenManager.validate(token)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = jwtTokenManager.createAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}
