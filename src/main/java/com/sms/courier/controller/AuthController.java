package com.sms.courier.controller;

import com.sms.courier.security.oauth.AuthType;
import com.sms.courier.security.oauth.impl.NerkoAuthServiceImpl;
import com.sms.courier.security.pojo.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    @Autowired
    NerkoAuthServiceImpl nerkoAuthService;

    @GetMapping("/test")
    public LoginResult test(AuthType type, String code, String state) {
        return nerkoAuthService.createLoginResult(type, code);
    }
}
