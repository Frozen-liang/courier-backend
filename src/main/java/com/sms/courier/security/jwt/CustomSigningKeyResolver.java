package com.sms.courier.security.jwt;

import static com.sms.courier.utils.JwtUtils.TOKEN_TYPE;

import com.sms.courier.security.TokenType;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategyFactory;
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
        try {
            String type = (String) header.get(TOKEN_TYPE);
            TokenType tokenType = TokenType.valueOf(type);
            SatpSecurityStrategy satpSecurityStrategy = securityStrategyFactory.fetchSecurityStrategy(tokenType);
            return satpSecurityStrategy.generateSecretKey(header);
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
