package com.sms.satp.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.response.Response;
import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.pojo.LoginResult;
import com.sms.satp.utils.ResponseUtil;
import com.sms.satp.utils.SecurityUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenManager jwtTokenManager;

    public AuthenticationSuccessHandler(
        ObjectMapper objectMapper, JwtTokenManager jwtTokenManager) {
        this.objectMapper = objectMapper;
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response, Authentication authentication) {
        CustomUser customUser = SecurityUtil.getCustomUser(authentication);
        ResponseUtil.out(response, Response.ok(createLoginResult(customUser)), objectMapper);
    }

    private LoginResult createLoginResult(CustomUser customUser) {

        return LoginResult.builder().token(jwtTokenManager.generateAccessToken(customUser)).build();
    }
}