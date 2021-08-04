package com.sms.courier.security.filter;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.security.jwt.JwtTokenManager;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

@DisplayName("JwtTokenFilter Test")
public class JwtTokenFilterTest {

    JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain chain = mock(FilterChain.class);

    UserTokenFilter userTokenFilter = new UserTokenFilter(jwtTokenManager);

    @Test
    @DisplayName("With incorrect authentication header format")
    public void withIncorrectAuthenticationHeaderFormat() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("token");
        doNothing().when(chain).doFilter(request, response);
        userTokenFilter.doFilterInternal(request, response, chain);
    }

    @Test
    @DisplayName("Token authentication failed")
    public void tokenAuthenticationFailed() throws ServletException, IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(jwtTokenManager.validate("token")).thenReturn(false);
        doNothing().when(chain).doFilter(request, response);
        userTokenFilter.doFilterInternal(request, response, chain);
    }

    @Test
    @DisplayName("Token authentication success")
    public void tokenAuthenticationSuccess() throws ServletException, IOException {
        Authentication authentication = mock(Authentication.class);
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(jwtTokenManager.validate("token")).thenReturn(true);
        when(jwtTokenManager.createAuthentication("token")).thenReturn(authentication);
        doNothing().when(chain).doFilter(request, response);
        userTokenFilter.doFilterInternal(request, response, chain);
    }

}