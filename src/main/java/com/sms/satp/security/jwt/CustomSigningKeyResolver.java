package com.sms.satp.security.jwt;

import static com.sms.satp.utils.JwtUtils.TOKEN_TYPE;

import com.sms.satp.security.TokenType;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import java.security.Key;
import org.springframework.stereotype.Component;

@Component
public class CustomSigningKeyResolver implements SigningKeyResolver {

    private final SecurityStrategyFactory securityStrategyFactory;

    public CustomSigningKeyResolver(SecurityStrategyFactory securityStrategyFactory) {
        this.securityStrategyFactory = securityStrategyFactory;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        String type = (String) claims.getOrDefault(TOKEN_TYPE, TokenType.USER.name());
        TokenType tokenType = TokenType.valueOf(type);
        SatpSecurityStrategy satpSecurityStrategy = securityStrategyFactory.fetchSecurityStrategy(tokenType);
        try {
            return satpSecurityStrategy.generateSecretKeySpec(header, claims);
        } catch (Exception e) {
            throw new UnsupportedJwtException(
                "The specified SigningKeyResolver implementation does not support "
                    + "Claims JWS signing key resolution.", e);
        }
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support "
            + "Claims JWS signing key resolution.");
    }
}
