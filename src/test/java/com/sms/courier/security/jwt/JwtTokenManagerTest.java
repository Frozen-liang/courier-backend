package com.sms.courier.security.jwt;

import static com.sms.courier.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.courier.utils.JwtUtils.TOKEN_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.strategy.SecurityStrategyFactory;
import com.sms.courier.security.strategy.impl.UserSecurityStrategy;
import com.sms.courier.utils.JwtUtils;
import com.sms.courier.utils.SecurityUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtTokenManagerTest {

    private static String token = "token";
    private static String id = "id";
    private static String userTokenType = "USER";
    private static String engineTokenType = "ENGINE";
    private static JwsHeader<?> jwsHeader;
    private static final MockedStatic<JwtUtils> JWT_UTILS_MOCKED_STATIC;
    private static final MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;
    private static final LocalDate EXPIRED_DATE = LocalDate.now();

    static {
        jwsHeader = mock(JwsHeader.class);
        when(jwsHeader.get(TOKEN_USER_ID)).thenReturn(id);
        when(jwsHeader.get(TOKEN_TYPE)).thenReturn(userTokenType);
        JWT_UTILS_MOCKED_STATIC = Mockito.mockStatic(JwtUtils.class);
        SECURITY_UTIL_MOCKED_STATIC = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        JWT_UTILS_MOCKED_STATIC.close();
        SECURITY_UTIL_MOCKED_STATIC.close();
    }

    SecurityStrategyFactory securityStrategyFactory = mock(SecurityStrategyFactory.class);
    SigningKeyResolver signingKeyResolver = mock(SigningKeyResolver.class);
    UserSecurityStrategy userSecurityStrategy = mock(UserSecurityStrategy.class);
    JwtTokenManager jwtTokenManager = new JwtTokenManager( securityStrategyFactory,
        signingKeyResolver);

    @Test
    @DisplayName("Generate token for user successfully")
    public void generateAccessTokenTestSuccess() {
        when(securityStrategyFactory.fetchSecurityStrategy(TokenType.USER)).thenReturn(userSecurityStrategy);
        Duration duration = Duration.ofDays(7);
        when(userSecurityStrategy.obtainTokenExpirationTime()).thenReturn(duration);
        CustomUser customUser = new CustomUser("username", "password",
            Arrays.asList(new SimpleGrantedAuthority("role1"), new SimpleGrantedAuthority("role2")),
            "id", "name", "nickname", TokenType.USER, EXPIRED_DATE);
        String token = "123";
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.encodeJwt(any(CustomUser.class),
            any(SigningKeyResolver.class), any(Duration.class))).thenReturn(Optional.of(token));
        String accessToken = jwtTokenManager.generateAccessToken(customUser);
        assertThat(accessToken).isEqualTo(token);
    }

    @Test
    @DisplayName("Failed to generate token for user")
    public void generateAccessTokenTest() {
        when(securityStrategyFactory.fetchSecurityStrategy(TokenType.USER)).thenReturn(userSecurityStrategy);
        Duration duration = Duration.ofDays(7);
        when(userSecurityStrategy.obtainTokenExpirationTime()).thenReturn(duration);
        CustomUser customUser = new CustomUser("username", "password",
            Arrays.asList(new SimpleGrantedAuthority("role1"), new SimpleGrantedAuthority("role2")),
            "id", "name", "nickname", TokenType.USER, EXPIRED_DATE);
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.encodeJwt(any(CustomUser.class),
            any(SigningKeyResolver.class), any(Duration.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> jwtTokenManager.generateAccessToken(customUser))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Parsing out identity information based on token")
    public void createAuthenticationTest() {
        String username = "username";
        String email = "email";
        String groupId = "groupId";
        String nickname = "nickname";
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        when(jwsHeader.get(TOKEN_TYPE)).thenReturn(userTokenType);
        when(securityStrategyFactory.fetchSecurityStrategy(any())).thenReturn(userSecurityStrategy);
        Authentication mockAuthentication = mock(Authentication.class);
        when(userSecurityStrategy.createAuthentication(any())).thenReturn(mockAuthentication);
        SECURITY_UTIL_MOCKED_STATIC.when(() -> SecurityUtil.newAuthentication(id, email, username, nickname,
            Collections.emptyList(), TokenType.USER, EXPIRED_DATE)).thenReturn(mockAuthentication);
        Authentication authentication = jwtTokenManager.createAuthentication(token);
        assertThat(authentication).isEqualTo(mockAuthentication);
    }



    @Test
    @DisplayName("Failed to Parse out identity information based on token because of exception")
    public void createAuthenticationWhileThrowExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        Authentication authentication = jwtTokenManager.createAuthentication(token);
        assertThat(authentication).isEqualTo(null);
    }

    @Test
    @DisplayName("Failed to Parse out identity information based on token because of user id is not exist")
    public void createAuthenticationFailedTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        Authentication authentication = jwtTokenManager.createAuthentication(token);
        assertThat(authentication).isEqualTo(null);
    }

    @Test
    @DisplayName("Parsing out user id based on token")
    public void getUserIdTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        assertThat(jwtTokenManager.getUserId(token)).isEqualTo(id);
    }

    @Test
    @DisplayName("Parsing out tokenType id based on token")
    public void getTokenTypeTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        when(jwsHeader.get(TOKEN_TYPE)).thenReturn(engineTokenType);
        assertThat(jwtTokenManager.getTokenType(token)).isEqualTo(engineTokenType);
    }

    @Test
    @DisplayName("Verify token")
    public void validateTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(jwsHeader);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(true);
    }

    @Test
    @DisplayName("Throw SignatureException when verify token")
    public void validateSignatureExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(SignatureException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw MalformedJwtException when verify token")
    public void validateMalformedJwtExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(MalformedJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw ExpiredJwtException when verify token")
    public void validateExpiredJwtExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(ExpiredJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw UnsupportedJwtException when verify token")
    public void validateUnsupportedJwtExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(UnsupportedJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when verify token")
    public void validateIllegalArgumentExceptionTest() {
        JWT_UTILS_MOCKED_STATIC.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(IllegalArgumentException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

}