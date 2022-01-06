package com.sms.courier.security.oauth.impl;

import static com.sms.courier.common.constant.Constants.BEARER;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.OAuthUrlResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.oauth.OAuthProperties;
import com.sms.courier.security.oauth.OAuthService;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.security.oauth.OAuthType;
import com.sms.courier.security.oauth.TokenEndpointResponse;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.pojo.LoginResult;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.ExceptionUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class AbstractOAuthService implements OAuthService {

    protected final RestTemplate restTemplate;
    protected final OAuthSettingRepository oauthSettingRepository;
    protected final UserRepository userRepository;
    protected final JwtTokenManager jwtTokenManager;
    protected final OAuthProperties oauthProperties;
    private static final String GRANT_TYPE = "grant_type";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String CODE = "code";
    protected final LocalDate expiredDate = LocalDate.of(2222, 1, 1);
    private static final String LOGIN_URL = "%s?response_type=code&client_id=%s&scope=%s&redirect_uri=%s";

    public AbstractOAuthService(RestTemplate restTemplate,
        OAuthSettingRepository oauthSettingRepository, UserRepository userRepository,
        JwtTokenManager jwtTokenManager, OAuthProperties oauthProperties) {
        this.restTemplate = restTemplate;
        this.oauthSettingRepository = oauthSettingRepository;
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.oauthProperties = oauthProperties;
    }


    @Override
    public LoginResult createLoginResult(OAuthType type, String code) {
        try {
            OAuthSettingEntity authSetting = getAuthSetting(type);
            HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = createHttpEntity(code, authSetting);
            ResponseEntity<TokenEndpointResponse> responseEntity = restTemplate
                .postForEntity(authSetting.getTokenUri(), httpEntity, TokenEndpointResponse.class);
            TokenEndpointResponse body = responseEntity.getBody();
            String accessToken = getAccessToken(body);
            UserEntity userEntity = getUserInfo(authSetting.getUserInfoUri(), authSetting.getAuthType(), accessToken);
            CustomUser user = CustomUser.createUser(userEntity.getId(), userEntity.getUsername());
            String token = jwtTokenManager.generateAccessToken(user);
            return LoginResult.builder().token(token).build();
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (RestClientException e) {
            log.error("Oauth fail!", e);
            throw ExceptionUtils.mpe("Oauth fail!");
        } catch (Exception e) {
            log.error("System error!", e);
            throw ExceptionUtils.mpe(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public List<OAuthUrlResponse> getOAuthUrl() {
        List<OAuthUrlResponse> responses = new ArrayList<>();
        List<OAuthSettingEntity> list = oauthSettingRepository.findAll();
        for (OAuthSettingEntity authSetting : list) {
            OAuthUrlResponse oauthUrlResponse = new OAuthUrlResponse();
            String url = String
                .format(LOGIN_URL, authSetting.getAuthUri(), authSetting.getClientId(), authSetting.getScope(),
                    oauthProperties.getRedirectUri(authSetting.getAuthType()));
            oauthUrlResponse.setName(authSetting.getAuthType().getName());
            oauthUrlResponse.setUrl(url);
            oauthUrlResponse.setIcon(authSetting.getIcon());
            responses.add(oauthUrlResponse);
        }
        return responses;
    }

    protected OAuthSettingEntity getAuthSetting(OAuthType type) {
        return oauthSettingRepository.findByAuthType(type)
            .orElseThrow(() -> ExceptionUtils.mpe("Nonsupport %s login!", type));
    }

    public abstract UserEntity getUserInfo(String userInfoUri, OAuthType type, String accessToken);

    protected void verifyUser(UserEntity userEntity) {
        Optional<UserEntity> optional = userRepository.findByEmail(userEntity.getEmail());
        if (optional.isEmpty()) {
            if (userRepository.existsByUsername(userEntity.getUsername())) {
                userEntity.setUsername(RandomStringUtils.random(8, true, false));
            }
            userRepository.save(userEntity);
            return;
        }
        userEntity.setId(optional.get().getId());
    }

    private String getAccessToken(TokenEndpointResponse body) {
        if (Objects.isNull(body) || StringUtils.isBlank(body.getAccessToken())) {
            throw ExceptionUtils.mpe("Login fail!");
        }
        if (!BEARER.trim().equalsIgnoreCase(body.getTokenType())) {
            throw ExceptionUtils.mpe("Only support Bearer token type!");
        }
        return body.getAccessToken();
    }

    private HttpEntity<LinkedMultiValueMap<String, String>> createHttpEntity(String code,
        OAuthSettingEntity authSetting) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.set(GRANT_TYPE, "authorization_code");
        body.set(REDIRECT_URI, oauthProperties.getRedirectUri(authSetting.getAuthType()));
        body.set(CODE, code);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(authSetting.getClientId(), AesUtil.decrypt(authSetting.getClientSecret()));
        return new HttpEntity<>(body, httpHeaders);
    }

}
