package com.sms.courier.security.oauth.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.response.OAuthUrlResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.oauth.OAuthProperties;
import com.sms.courier.security.oauth.OAuthService;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.security.oauth.TokenEndpointResponse;
import com.sms.courier.security.pojo.LoginResult;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class OAuthServiceImplTest {

    private final RestTemplate restTemplate = mock(RestTemplate.class);
    private final OAuthSettingRepository oauthSettingRepository = mock(OAuthSettingRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtTokenManager jwtTokenManager = mock(JwtTokenManager.class);
    private final OAuthProperties oauthProperties = mock(OAuthProperties.class);
    private final OAuthService oAuthService = new OAuthServiceImpl(restTemplate, oauthSettingRepository,
        userRepository, jwtTokenManager, oauthProperties);
    private final OAuthSettingEntity oAuthSettingEntity =
        OAuthSettingEntity.builder().url("http://test.com").clientId("courier")
            .clientSecret("sD3Sq/kIawwsyujU3Tnq1NMLlY7P8Bh1rd628pOJHcU=").authPath("/auth").emailKey("email")
            .tokenPath("/token").userInfoPath("/userinfo").scope("scope").build();


    @DisplayName("Test createLoginResult")
    @Test
    public void createLoginResult_test() {
        String code = "code";
        when(oauthSettingRepository.findByName(anyString())).thenReturn(Optional.of(oAuthSettingEntity));
        TokenEndpointResponse tokenEndpointResponse = new TokenEndpointResponse();
        tokenEndpointResponse.setTokenType("Bearer");
        tokenEndpointResponse.setAccessToken("accessToken");
        ResponseEntity<TokenEndpointResponse> responseEntity = new ResponseEntity<>(tokenEndpointResponse,
            HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), any(Class.class))).thenReturn(responseEntity);
        String userInfo = "{\"email\":\"email\"}";
        ResponseEntity<String> nerkoUserResponseEntity = new ResponseEntity<>(userInfo, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
            .thenReturn(nerkoUserResponseEntity);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(UserEntity.builder().id("id").username("username").build());
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        String token = "token";
        when(jwtTokenManager.generateAccessToken(any())).thenReturn(token);
        LoginResult loginResult = oAuthService.createLoginResult("Nerko", code);
        assertThat(loginResult.getToken()).isEqualTo(token);
    }

    @DisplayName("An exception occurred while createLoginResult")
    @Test
    public void createLoginResult_custom_exception_test() {
        String code = "code";
        when(oauthSettingRepository.findByName(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> oAuthService.createLoginResult("Nerko", code)).isInstanceOf(
            ApiTestPlatformException.class);
    }

    @DisplayName("Test getOAuthUrl")
    @Test
    public void getOAuthUrl_test() {
        when(oauthSettingRepository.findAll()).thenReturn(List.of(oAuthSettingEntity));
        List<OAuthUrlResponse> responses = oAuthService.getOAuthUrl();
        assertThat(responses).hasSize(1);
    }

}
