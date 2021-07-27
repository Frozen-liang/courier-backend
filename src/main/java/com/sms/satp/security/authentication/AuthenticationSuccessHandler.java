package com.sms.satp.security.authentication;

import static com.sms.satp.common.enums.OperationType.LOGIN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.log.LogEntity;
import com.sms.satp.security.jwt.JwtTokenManager;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.pojo.LoginResult;
import com.sms.satp.service.LogService;
import com.sms.satp.utils.ResponseUtil;
import com.sms.satp.utils.SecurityUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenManager jwtTokenManager;
    private final LogService logService;

    public AuthenticationSuccessHandler(
        ObjectMapper objectMapper, JwtTokenManager jwtTokenManager, LogService logService) {
        this.objectMapper = objectMapper;
        this.jwtTokenManager = jwtTokenManager;
        this.logService = logService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response, Authentication authentication) {
        CustomUser customUser = SecurityUtil.getCustomUser(authentication);
        ResponseUtil.out(response, Response.ok(createLoginResult(customUser)), objectMapper);
        saveLoginLog(customUser);
    }

    private void saveLoginLog(CustomUser customUser) {
        LogEntity logEntity = LogEntity.builder().operator(customUser.getUsername()).operatorId(customUser.getId())
            .operationDesc("登录")
            .operationType(LOGIN).build();
        logService.add(logEntity);
    }

    private LoginResult createLoginResult(CustomUser customUser) {

        return LoginResult.builder().token(jwtTokenManager.generateAccessToken(customUser)).build();
    }
}