package com.sms.satp.security.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.security.AccessTokenProperties;
import com.sms.satp.security.strategy.impl.UserSecurityStrategy;
import io.jsonwebtoken.Claims;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserSecurityStrategyTest {

    AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    UserSecurityStrategy userSecurityStrategy = new UserSecurityStrategy(accessTokenProperties);

    @Test
    @DisplayName("Test user-generateSecretKey")
    void generateSecretKeyTest() {
        String secretKey = "123";
        when(accessTokenProperties.getSecretKey()).thenReturn(secretKey);
        Claims claims = mock(Claims.class);
        String key = userSecurityStrategy.generateSecretKey(claims);
        assertThat(key).isEqualTo(secretKey);
    }

    @Test
    @DisplayName("Test user-obtainTokenExpirationTime")
    void obtainTokenExpirationTimeTest() {
        Duration duration = Duration.ofSeconds(1);
        when(accessTokenProperties.getExpire()).thenReturn(duration);
        Duration result = userSecurityStrategy.obtainTokenExpirationTime();
        assertThat(result).isEqualTo(duration);
    }
}