package com.sms.courier.security.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.response.Response;
import com.sms.courier.utils.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Slf4j
public class CustomLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomLoginUrlAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtil.out(response, Response.error(
            "40001", "Unauthorized! " + authException.getMessage()), objectMapper);
    }


}