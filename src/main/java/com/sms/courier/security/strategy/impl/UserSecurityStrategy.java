package com.sms.courier.security.strategy.impl;

import com.sms.courier.dto.UserEntityAuthority;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategy;
import com.sms.courier.service.UserService;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import org.springframework.security.core.Authentication;

@SecurityStrategy(type = TokenType.USER)
public class UserSecurityStrategy implements SatpSecurityStrategy {

    private final AccessTokenProperties accessTokenProperties;
    private final UserService userService;

    public UserSecurityStrategy(AccessTokenProperties accessTokenProperties,
        UserService userService) {
        this.accessTokenProperties = accessTokenProperties;
        this.userService = userService;
    }

    @Override
    public Key generateSecretKey(JwsHeader<?> jwsHeader) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getUserSecretKey()));
    }

    @Override
    public Duration obtainTokenExpirationTime() {
        return accessTokenProperties.getUserExpire();
    }

    @Override
    public Authentication createAuthentication(String id) {
        UserEntityAuthority userEntityAuthority = userService.getUserDetailsByUserId(id);
        UserEntity userEntity = userEntityAuthority.getUserEntity();
        return SecurityUtil.newAuthentication(id, userEntity.getEmail(),
            userEntity.getUsername(), userEntity.getNickname(),
            userEntityAuthority.getAuthorities(), TokenType.USER, userEntity.getExpiredDate());
    }
}