package com.sms.satp.security.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.security.AccessTokenProperties;
import com.sms.satp.security.strategy.impl.UserSecurityStrategy;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserSecurityStrategyTest {

    AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    UserSecurityStrategy userSecurityStrategy = new UserSecurityStrategy(accessTokenProperties);

    @Test
    @DisplayName("Test user-generateSecretKey")
    void generateSecretKeyTest() {
        String secretKey = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        when(accessTokenProperties.getUserSecretKey()).thenReturn(secretKey);
        JwsHeader<?> jwsHeader = mock(JwsHeader.class);
        Key key = userSecurityStrategy.generateSecretKey(jwsHeader);
        assertThat(key).isEqualTo(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)));
    }

    @Test
    @DisplayName("Test user-obtainTokenExpirationTime")
    void obtainTokenExpirationTimeTest() {
        Duration duration = Duration.ofSeconds(1);
        when(accessTokenProperties.getUserExpire()).thenReturn(duration);
        Duration result = userSecurityStrategy.obtainTokenExpirationTime();
        assertThat(result).isEqualTo(duration);
    }
}