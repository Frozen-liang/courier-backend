package com.sms.satp.security.jwt;

import static com.sms.satp.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.satp.utils.JwtUtils.TOKEN_USER_ID;

import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.security.TokenType;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
import com.sms.satp.service.UserGroupService;
import com.sms.satp.utils.JwtUtils;
import com.sms.satp.utils.SecurityUtil;
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

    private final UserRepository userRepository;
    private final UserGroupService userGroupService;
    private final SecurityStrategyFactory securityStrategyFactory;
    private final SigningKeyResolver signingKeyResolver;

    public JwtTokenManager(UserRepository userRepository, UserGroupService userGroupService,
        SecurityStrategyFactory securityStrategyFactory,
        SigningKeyResolver signingKeyResolver) {
        this.userRepository = userRepository;
        this.userGroupService = userGroupService;
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

            Optional<UserEntity> optional = userRepository.findById(id);
            if (optional.isPresent()) {
                UserEntity userEntity = optional.get();
                return SecurityUtil.newAuthentication(id, userEntity.getEmail(), userEntity.getUsername(),
                    userGroupService.getAuthoritiesByUserGroup(userEntity.getGroupId()), tokenType);
            }
            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    public String getUserId(String token) {
        JwsHeader<?> jwsHeader = JwtUtils.decodeJwt(token, signingKeyResolver);
        return  (String) jwsHeader.get(TOKEN_USER_ID);
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
