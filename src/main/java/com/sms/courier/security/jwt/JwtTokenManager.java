package com.sms.courier.security.jwt;

import static com.sms.courier.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.courier.utils.JwtUtils.TOKEN_USER_ID;

import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategyFactory;
import com.sms.courier.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import javax.security.auth.login.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenManager {

    private final SecurityStrategyFactory securityStrategyFactory;
    private final SigningKeyResolver signingKeyResolver;

    public JwtTokenManager(SecurityStrategyFactory securityStrategyFactory,
        SigningKeyResolver signingKeyResolver) {
        this.securityStrategyFactory = securityStrategyFactory;
        this.signingKeyResolver = signingKeyResolver;
    }

    public String generateAccessToken(CustomUser user) {
        SatpSecurityStrategy satpSecurityStrategy = securityStrategyFactory.fetchSecurityStrategy(user.getTokenType());
        Duration expirationTime = satpSecurityStrategy.obtainTokenExpirationTime();
        Optional<String> tokenOptional = JwtUtils.encodeJwt(user, signingKeyResolver, expirationTime);
        return tokenOptional.orElseThrow(() -> new RuntimeException(new AccountNotFoundException()));
    }

    public Authentication createAuthentication(String token) {
        try {
            JwsHeader<?> jwsHeader = JwtUtils.decodeJwt(token, signingKeyResolver);
            String id = (String) jwsHeader.get(TOKEN_USER_ID);
            TokenType tokenType = TokenType.valueOf((String) jwsHeader.get(TOKEN_TYPE));
            SatpSecurityStrategy satpSecurityStrategy = securityStrategyFactory.fetchSecurityStrategy(tokenType);
            return satpSecurityStrategy.createAuthentication(id);
        } catch (Exception exception) {
            log.error("Create authentication error!", exception);
            return null;
        }
    }


    public String getUserId(String token) {
        JwsHeader<?> jwsHeader = JwtUtils.decodeJwt(token, signingKeyResolver);
        return (String) jwsHeader.get(TOKEN_USER_ID);
    }

    public String getTokenType(String token) {
        JwsHeader<?> jwsHeader = JwtUtils.decodeJwt(token, signingKeyResolver);
        return (String) jwsHeader.get(TOKEN_TYPE);
    }

    public boolean validate(String token) {
        try {
            return Objects.nonNull(JwtUtils.decodeJwt(token, signingKeyResolver));
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature!", ex);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token!", ex);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token!", ex);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token!", ex);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty!", ex);
        }
        return false;
    }

}
