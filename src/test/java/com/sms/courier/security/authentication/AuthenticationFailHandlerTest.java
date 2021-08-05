package com.sms.courier.security.authentication;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

@DisplayName("AuthenticationFailHandler test")
public class AuthenticationFailHandlerTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    AuthenticationException authenticationException = mock(AuthenticationException.class);

    AuthenticationFailHandler authenticationFailHandler = new AuthenticationFailHandler(objectMapper);

    @Test
    @DisplayName("onAuthenticationFailure test")
    void onAuthenticationFailure() {
        when(authenticationException.getMessage()).thenReturn("exception");
        authenticationFailHandler.onAuthenticationFailure(request, response, authenticationException);
    }
}