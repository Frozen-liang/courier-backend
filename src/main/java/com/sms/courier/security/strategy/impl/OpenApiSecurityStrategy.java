package com.sms.courier.security.strategy.impl;

import static com.sms.courier.utils.Assert.isFalse;
import static com.sms.courier.utils.Assert.isTrue;

import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import com.sms.courier.repository.OpenApiSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.strategy.SatpSecurityStrategy;
import com.sms.courier.security.strategy.SecurityStrategy;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SecurityStrategy(type = TokenType.OPEN_API)
public class OpenApiSecurityStrategy implements SatpSecurityStrategy {

    private final AccessTokenProperties accessTokenProperties;
    private final OpenApiSettingRepository openApiSettingRepository;
    @Value("${courier.open.api.roles:[]}")
    private final List<String> roles = new ArrayList<>();

    public OpenApiSecurityStrategy(AccessTokenProperties accessTokenProperties,
        OpenApiSettingRepository openApiSettingRepository) {
        this.accessTokenProperties = accessTokenProperties;
        this.openApiSettingRepository = openApiSettingRepository;
    }

    @Override
    public Key generateSecretKey(JwsHeader<?> jwsHeader) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getOpenApiSecretKey()));
    }

    @Override
    public Duration obtainTokenExpirationTime() {
        return accessTokenProperties.getOpenApiExpire();
    }

    @Override
    public Authentication createAuthentication(String id) {
        OpenApiSettingEntity openApiSetting = openApiSettingRepository.findById(id)
            .orElseThrow(() -> ExceptionUtils.mpe("The open client not exists! "
                + "id = %s", id));
        isFalse(openApiSetting.isRemoved(), "The open client unable! name = %s!", openApiSetting.getName());
        isTrue(openApiSetting.getExpireTime().isAfter(LocalDateTime.now()), "The open client expire! name = %s!",
            openApiSetting.getName());
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = this.roles.stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
        return SecurityUtil
            .newAuthentication(id, "", openApiSetting.getName(), "open-client", simpleGrantedAuthorities,
                TokenType.OPEN_API,
                LocalDate.now());
    }
}