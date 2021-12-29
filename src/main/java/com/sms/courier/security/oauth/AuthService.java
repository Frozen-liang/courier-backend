package com.sms.courier.security.oauth;

import com.sms.courier.security.pojo.LoginResult;

public interface AuthService {

    LoginResult createLoginResult(AuthType type, String code);
}
