package com.sms.satp.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.response.Response;
import com.sms.satp.utils.ResponseUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void handle(HttpServletRequest request,
        HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.error("[RestAccessDeniedHandler] - handle: ", accessDeniedException);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ResponseUtil.out(response, Response.error(
            "40001", "Unauthorized! " + accessDeniedException.getMessage()), objectMapper);
    }

}