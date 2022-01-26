package com.sms.courier.security.strategy.impl;

import static com.sms.courier.common.enums.RoleType.ENGINE;

import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategy;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SecurityStrategy(type = TokenType.ENGINE)
public class EngineSecurityStrategy implements SatpSecurityStrategy {

    private final AccessTokenProperties accessTokenProperties;
    private final SystemRoleRepository roleRepository;

    public EngineSecurityStrategy(AccessTokenProperties accessTokenProperties,
        SystemRoleRepository roleRepository) {
        this.accessTokenProperties = accessTokenProperties;
        this.roleRepository = roleRepository;
    }

    @Override
    public Key generateSecretKey(JwsHeader<?> jwsHeader) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getEngineSecretKey()));
    }

    @Override
    public Duration obtainTokenExpirationTime() {
        return accessTokenProperties.getEngineExpire();
    }

    @Override
    public Authentication createAuthentication(String id) {
        List<SimpleGrantedAuthority> roles = roleRepository.findAllByRoleType(ENGINE)
            .map(SystemRoleEntity::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return SecurityUtil.newAuthentication(id, "", "engine", "engine", roles, TokenType.ENGINE, LocalDate.now());
    }
}