package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.OAUTH;

import com.sms.courier.dto.response.OAuthUrlResponse;
import com.sms.courier.security.oauth.OAuthService;
import com.sms.courier.security.oauth.OAuthType;
import com.sms.courier.security.pojo.LoginResult;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(OAUTH)
@RestController
public class OAuthController {

    private final OAuthService oauthService;

    public OAuthController(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/callback")
    public LoginResult callback(OAuthType type, String code) {
        return oauthService.createLoginResult(type, code);
    }

    @GetMapping("/url")
    public List<OAuthUrlResponse> getOAuthUrl() {
        return oauthService.getOAuthUrl();
    }
}
