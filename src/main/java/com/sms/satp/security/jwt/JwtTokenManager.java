package com.sms.satp.security.jwt;

import static com.sms.satp.common.enums.RoleType.ENGINE;
import static com.sms.satp.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.satp.utils.JwtUtils.TOKEN_USER_ID;

import com.sms.satp.dto.UserEntityAuthority;
import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.security.TokenType;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.strategy.SatpSecurityStrategy;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
import com.sms.satp.service.UserService;
import com.sms.satp.utils.JwtUtils;
import com.sms.satp.utils.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.security.auth.login.AccountNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenManager {

    private final UserService userService;
    private final SystemRoleRepository roleRepository;
    private final SecurityStrategyFactory securityStrategyFactory;
    private final SigningKeyResolver signingKeyResolver;

    public JwtTokenManager(UserService userService,
        SystemRoleRepository roleRepository,
        SecurityStrategyFactory securityStrategyFactory,
        SigningKeyResolver signingKeyResolver) {
        this.userService = userService;
        this.roleRepository = roleRepository;
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
            Authentication authentication;
            switch (tokenType) {
                case USER:
                    authentication = createUserAuthentication(id, tokenType);
                    break;
                case ENGINE:
                    authentication = createEngineAuthentication(id, tokenType);
                    break;
                default:
                    authentication = null;
            }
            return authentication;
        } catch (Exception exception) {
            return null;
        }
    }

    private Authentication createUserAuthentication(String id, TokenType tokenType) {
        UserEntityAuthority userEntityAuthority = userService.getUserDetailsByUserId(id);
        return SecurityUtil.newAuthentication(id, userEntityAuthority.getUserEntity().getEmail(),
            userEntityAuthority.getUserEntity().getUsername(),
            userEntityAuthority.getAuthorities(), tokenType);

    }

    private Authentication createEngineAuthentication(String id, TokenType tokenType) {
        List<SimpleGrantedAuthority> roles = roleRepository.findAllByRoleType(ENGINE)
            .map(SystemRoleEntity::getName).map(SimpleGrantedAuthority::new
            ).collect(Collectors.toList());
        return SecurityUtil.newAuthentication(id, "", "engine", roles, tokenType);

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
