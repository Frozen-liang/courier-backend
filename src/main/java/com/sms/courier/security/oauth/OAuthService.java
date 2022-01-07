package com.sms.courier.security.oauth;

import com.sms.courier.dto.response.OAuthUrlResponse;
import com.sms.courier.security.pojo.LoginResult;
import java.util.List;

public interface OAuthService {

    LoginResult createLoginResult(OAuthType type, String code);

    List<OAuthUrlResponse> getOAuthUrl();
}
