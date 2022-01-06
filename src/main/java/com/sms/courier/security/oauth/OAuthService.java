package com.sms.courier.security.oauth;

import com.sms.courier.security.pojo.LoginResult;

public interface OAuthService {

    LoginResult createLoginResult(OAuthType type, String code);

    String getLoginUrlByAuthType(OAuthType type);
}
