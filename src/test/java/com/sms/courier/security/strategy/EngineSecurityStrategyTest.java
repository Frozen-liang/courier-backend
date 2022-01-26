package com.sms.courier.security.strategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.RoleType;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.security.AccessTokenProperties;
import com.sms.courier.security.strategy.impl.EngineSecurityStrategy;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

public class EngineSecurityStrategyTest {

    AccessTokenProperties accessTokenProperties = mock(AccessTokenProperties.class);
    SystemRoleRepository roleRepository = mock(SystemRoleRepository.class);
    EngineSecurityStrategy engineSecurityStrategy = new EngineSecurityStrategy(accessTokenProperties, roleRepository);

    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    static {
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    @Test
    @DisplayName("Test engine-generateSecretKey")
    void generateSecretKeyTest() {
        String secretKey = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
        when(accessTokenProperties.getEngineSecretKey()).thenReturn(secretKey);
        JwsHeader<?> jwsHeader = mock(JwsHeader.class);
        Key key = engineSecurityStrategy.generateSecretKey(jwsHeader);
        assertThat(key).isEqualTo(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)));
    }

    @Test
    @DisplayName("Test engine-obtainTokenExpirationTime")
    void obtainTokenExpirationTimeTest() {
        Duration duration = Duration.ofSeconds(1);
        when(accessTokenProperties.getEngineExpire()).thenReturn(duration);
        Duration result = engineSecurityStrategy.obtainTokenExpirationTime();
        assertThat(result).isEqualTo(duration);
    }

    @Test
    @DisplayName("Test engine-createAuthentication")
    public void createAuthentication() {
        Stream<SystemRoleEntity> roles = Stream.of(SystemRoleEntity.builder().name("Admin").build());
        when(roleRepository.findAllByRoleType(RoleType.ENGINE))
            .thenReturn(roles);
        Authentication mockAuthentication = mock(Authentication.class);
        SECURITY_UTIL_MOCKED_STATIC.when(() -> SecurityUtil.newAuthentication(anyString(), anyString(), anyString(),
            anyString(),
            any(), any(), any())).thenReturn(mockAuthentication);
        Authentication authentication = engineSecurityStrategy.createAuthentication("engine");
        assertThat(authentication).isEqualTo(mockAuthentication);
    }
}