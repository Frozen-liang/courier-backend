package com.sms.courier.security.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import com.sms.courier.repository.OpenApiSettingRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.strategy.impl.OpenApiSecurityStrategy;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

public class OpenApiSecurityStrategyTest {

    AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    OpenApiSettingRepository openApiSettingRepository = mock(OpenApiSettingRepository.class);
    OpenApiSecurityStrategy openApiSecurityStrategy = new OpenApiSecurityStrategy(accessTokenProperties,
        openApiSettingRepository);

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test open-api-generateSecretKey")
    void generateSecretKeyTest() {
        String secretKey = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        when(accessTokenProperties.getOpenApiSecretKey()).thenReturn(secretKey);
        JwsHeader<?> jwsHeader = mock(JwsHeader.class);
        Key key = openApiSecurityStrategy.generateSecretKey(jwsHeader);
        assertThat(key).isEqualTo(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)));
    }

    @Test
    @DisplayName("Test open-api-obtainTokenExpirationTime")
    void obtainTokenExpirationTimeTest() {
        Duration duration = Duration.ofSeconds(1);
        when(accessTokenProperties.getOpenApiExpire()).thenReturn(duration);
        Duration result = openApiSecurityStrategy.obtainTokenExpirationTime();
        assertThat(result).isEqualTo(duration);
    }

    @Test
    @DisplayName("Test open-api-createAuthentication")
    public void createAuthentication() {
        Authentication mockAuthentication = mock(Authentication.class);
        SECURITY_UTIL_MOCKED_STATIC.when(() -> SecurityUtil.newAuthentication(anyString(), anyString(), anyString(),
            anyString(),
            any(), any(), any())).thenReturn(mockAuthentication);
        when(openApiSettingRepository.findById(any())).thenReturn(Optional.of(OpenApiSettingEntity.builder().name(
            "open").expireTime(
            LocalDateTime.MAX).build()));
        Authentication authentication = openApiSecurityStrategy.createAuthentication("open-api");
        assertThat(authentication).isEqualTo(mockAuthentication);
    }
}