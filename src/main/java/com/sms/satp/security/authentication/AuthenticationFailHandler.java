package com.sms.satp.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.response.Response;
import com.sms.satp.utils.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


@Slf4j
public class AuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationFailHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException e) {
        ResponseUtil.out(response, Response.error("40000", "Login fail! " + e.getMessage()),
            objectMapper);
    }

}