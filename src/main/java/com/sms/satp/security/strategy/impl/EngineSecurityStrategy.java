package com.sms.satp.security.strategy.impl;

import com.sms.satp.security.AccessTokenProperties;
import com.sms.satp.security.TokenType;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategy;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;

@SecurityStrategy(type = TokenType.ENGINE)
public class EngineSecurityStrategy implements SatpSecurityStrategy {

    private final AccessTokenProperties accessTokenProperties;

    public EngineSecurityStrategy(AccessTokenProperties accessTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
    }

    @Override
    public Key generateSecretKey(JwsHeader<?> jwsHeader) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getEngineSecretKey()));
    }

    @Override
    public Duration obtainTokenExpirationTime() {
        return accessTokenProperties.getEngineExpire();
    }
}