package com.sms.courier.security.strategy.impl;

import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategy;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;

@SecurityStrategy(type = TokenType.USER)
public class UserSecurityStrategy implements SatpSecurityStrategy {

    private final AccessTokenProperties accessTokenProperties;

    public UserSecurityStrategy(AccessTokenProperties accessTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
    }

    @Override
    public Key generateSecretKey(JwsHeader<?> jwsHeader) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getUserSecretKey()));
    }

    @Override
    public Duration obtainTokenExpirationTime() {
        return accessTokenProperties.getUserExpire();
    }
}