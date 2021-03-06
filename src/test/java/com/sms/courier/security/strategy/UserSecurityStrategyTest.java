package com.sms.courier.security.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.UserEntityAuthority;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.strategy.impl.UserSecurityStrategy;
import com.sms.courier.service.UserService;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Collections;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

public class UserSecurityStrategyTest {

    AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    UserService userService = mock(UserService.class);
    UserSecurityStrategy userSecurityStrategy = new UserSecurityStrategy(accessTokenProperties, userService);

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

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

    @Test
    @DisplayName("Test mock-createAuthentication")
    public void createAuthentication() {
        String id = ObjectId.get().toString();
        UserEntity user =
            UserEntity.builder().id(id).username("admin").nickname("nickname").email("test@123.com").build();
        when(userService.getUserDetailsByUserId(id))
            .thenReturn(UserEntityAuthority.builder().userEntity(user).authorities(
                Collections.emptyList()).build());
        Authentication mockAuthentication = mock(Authentication.class);
        SECURITY_UTIL_MOCKED_STATIC.when(() -> SecurityUtil.newAuthentication(anyString(), anyString(), anyString(),
            anyString(),
            any(), any(), any())).thenReturn(mockAuthentication);
        Authentication authentication = userSecurityStrategy.createAuthentication(id);
        assertThat(authentication).isEqualTo(mockAuthentication);
    }
}