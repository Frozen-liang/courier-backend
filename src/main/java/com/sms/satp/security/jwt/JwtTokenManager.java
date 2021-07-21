package com.sms.satp.security.jwt;

import static com.sms.satp.utils.JwtUtils.TOKEN_AUTHORITIES;
import static com.sms.satp.utils.JwtUtils.TOKEN_EMAIL;
import static com.sms.satp.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.satp.utils.JwtUtils.TOKEN_USERNAME;
import static com.sms.satp.utils.JwtUtils.TOKEN_USER_ID;

import com.sms.satp.security.TokenType;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
import com.sms.satp.utils.JwtUtils;
import com.sms.satp.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.security.auth.login.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @SuppressWarnings("unchecked")
    public Authentication createAuthentication(String token) {
        Claims claims = JwtUtils.decodeJwt(token, signingKeyResolver);
        String id = claims.get(TOKEN_USER_ID, String.class);
        String username = claims.get(TOKEN_USERNAME, String.class);
        String email = claims.get(TOKEN_EMAIL, String.class);
        TokenType tokenType = TokenType.valueOf(claims.get(TOKEN_TYPE, String.class));
        Collection<GrantedAuthority> authorities =
            ((List<String>) claims.get(TOKEN_AUTHORITIES)).stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return SecurityUtil.newAuthentication(id, email, username, authorities, tokenType);
    }

    public String getUserId(String token) {
        Claims claims = JwtUtils.decodeJwt(token, signingKeyResolver);
        return claims.get(TOKEN_USER_ID, String.class);
    }

    public boolean validate(String token) {
        try {
            return Objects.nonNull(JwtUtils.decodeJwt(token, signingKeyResolver));
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }

}
