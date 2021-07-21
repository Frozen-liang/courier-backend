package com.sms.satp.security.jwt;

import static com.sms.satp.utils.JwtUtils.TOKEN_AUTHORITIES;
import static com.sms.satp.utils.JwtUtils.TOKEN_EMAIL;
import static com.sms.satp.utils.JwtUtils.TOKEN_TYPE;
import static com.sms.satp.utils.JwtUtils.TOKEN_USERNAME;
import static com.sms.satp.utils.JwtUtils.TOKEN_USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.security.TokenType;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.security.strategy.SecurityStrategyFactory;
import com.sms.satp.security.strategy.impl.UserSecurityStrategy;
import com.sms.satp.utils.JwtUtils;
import com.sms.satp.utils.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtTokenManagerTest {

    private static String token = "token";
    private static String id = "id";
    private static String username = "username";
    private static String email = "email";
    private static String userTokenType = "USER";
    private static List<String> authorities = Arrays.asList("role1", "role2");
    private static Claims claims;
    private static MockedStatic<JwtUtils> jwtUtilsMockedStatic;
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    static {
        claims = mock(Claims.class);
        when(claims.get(TOKEN_USER_ID, String.class)).thenReturn(id);
        when(claims.get(TOKEN_USERNAME, String.class)).thenReturn(username);
        when(claims.get(TOKEN_EMAIL, String.class)).thenReturn(email);
        when(claims.get(TOKEN_TYPE, String.class)).thenReturn(userTokenType);
        when(claims.get(TOKEN_AUTHORITIES)).thenReturn(authorities);
        jwtUtilsMockedStatic = Mockito.mockStatic(JwtUtils.class);
        securityUtilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        jwtUtilsMockedStatic.close();
        securityUtilMockedStatic.close();
    }

    SecurityStrategyFactory securityStrategyFactory = mock(SecurityStrategyFactory.class);
    SigningKeyResolver signingKeyResolver = mock(SigningKeyResolver.class);
    UserSecurityStrategy userSecurityStrategy = mock(UserSecurityStrategy.class);

    JwtTokenManager jwtTokenManager = new JwtTokenManager(securityStrategyFactory, signingKeyResolver);

    @Test
    @DisplayName("Generate token for user successfully")
    public void generateAccessTokenTestSuccess() {
        when(securityStrategyFactory.fetchSecurityStrategy(TokenType.USER)).thenReturn(userSecurityStrategy);
        Duration duration = Duration.ofDays(7);
        when(userSecurityStrategy.obtainTokenExpirationTime()).thenReturn(duration);
        CustomUser customUser = new CustomUser("username", "password",
            Arrays.asList(new SimpleGrantedAuthority("role1"), new SimpleGrantedAuthority("role2")),
            "id", "name", TokenType.USER);
        String token = "123";
        jwtUtilsMockedStatic.when(() -> JwtUtils.encodeJwt(any(CustomUser.class),
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
            "id", "name", TokenType.USER);
        jwtUtilsMockedStatic.when(() -> JwtUtils.encodeJwt(any(CustomUser.class),
            any(SigningKeyResolver.class), any(Duration.class))).thenReturn(Optional.empty());
        assertThatThrownBy(() -> jwtTokenManager.generateAccessToken(customUser))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Parsing out identity information based on token")
    public void createAuthenticationTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(claims);
        Collection<GrantedAuthority> grantedAuthorities = authorities.stream()
            .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        Authentication mockAuthentication = mock(Authentication.class);
        securityUtilMockedStatic.when(() -> SecurityUtil.newAuthentication(id, email, username, grantedAuthorities,
            TokenType.USER)).thenReturn(mockAuthentication);
        Authentication authentication = jwtTokenManager.createAuthentication(token);
        assertThat(mockAuthentication).isEqualTo(authentication);
    }

    @Test
    @DisplayName("Parsing out user id based on token")
    public void getUserIdTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(claims);
        assertThat(jwtTokenManager.getUserId(token)).isEqualTo(id);
    }

    @Test
    @DisplayName("Verify token")
    public void validateTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenReturn(claims);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(true);
    }

    @Test
    @DisplayName("Throw SignatureException when verify token")
    public void validateSignatureExceptionTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(SignatureException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw MalformedJwtException when verify token")
    public void validateMalformedJwtExceptionTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(MalformedJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw ExpiredJwtException when verify token")
    public void validateExpiredJwtExceptionTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(ExpiredJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw UnsupportedJwtException when verify token")
    public void validateUnsupportedJwtExceptionTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(UnsupportedJwtException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

    @Test
    @DisplayName("Throw IllegalArgumentException when verify token")
    public void validateIllegalArgumentExceptionTest() {
        jwtUtilsMockedStatic.when(() -> JwtUtils.decodeJwt(any(String.class), any(SigningKeyResolver.class)))
            .thenThrow(IllegalArgumentException.class);
        assertThat(jwtTokenManager.validate(token)).isEqualTo(false);
    }

}