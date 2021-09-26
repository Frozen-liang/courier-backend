package com.sms.courier.security.authentication;

import static com.sms.courier.common.enums.OperationType.LOGIN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms.courier.common.response.Response;
import com.sms.courier.entity.log.LogEntity;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.pojo.LoginResult;
import com.sms.courier.service.LogService;
import com.sms.courier.utils.ResponseUtil;
import com.sms.courier.utils.SecurityUtil;
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
            .operationType(LOGIN).build();
        logService.add(logEntity);
    }

    private LoginResult createLoginResult(CustomUser customUser) {

        return LoginResult.builder().token(jwtTokenManager.generateAccessToken(customUser)).build();
    }
}