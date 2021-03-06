package com.sms.courier.security.oauth.impl;

import static com.sms.courier.common.constant.Constants.BEARER;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.OAuthUrlResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.initialize.constant.Initializer;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.oauth.OAuthProperties;
import com.sms.courier.security.oauth.OAuthService;
import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.security.oauth.TokenEndpointResponse;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.pojo.LoginResult;
import com.sms.courier.utils.AesUtil;
import com.sms.courier.utils.Assert;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class OAuthServiceImpl implements OAuthService {

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

    public OAuthServiceImpl(RestTemplate restTemplate,
        OAuthSettingRepository oauthSettingRepository, UserRepository userRepository,
        JwtTokenManager jwtTokenManager, OAuthProperties oauthProperties) {
        this.restTemplate = restTemplate;
        this.oauthSettingRepository = oauthSettingRepository;
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.oauthProperties = oauthProperties;
    }


    @Override
    public LoginResult createLoginResult(String name, String code) {
        try {
            OAuthSettingEntity authSetting = getAuthSetting(name);
            HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = createHttpEntity(code, authSetting);
            ResponseEntity<TokenEndpointResponse> responseEntity = restTemplate
                .postForEntity(authSetting.getUrl() + authSetting.getTokenPath(), httpEntity,
                    TokenEndpointResponse.class);
            TokenEndpointResponse body = responseEntity.getBody();
            String accessToken = getAccessToken(body);
            UserEntity userEntity = getUserInfo(authSetting, accessToken);
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
                .format(LOGIN_URL, authSetting.getUrl() + authSetting.getAuthPath(), authSetting.getClientId(),
                    authSetting.getScope(),
                    oauthProperties.getRedirectUri(authSetting.getName()));
            oauthUrlResponse.setName(authSetting.getName());
            oauthUrlResponse.setUrl(url);
            oauthUrlResponse.setIcon(authSetting.getIcon());
            responses.add(oauthUrlResponse);
        }
        return responses;
    }

    protected OAuthSettingEntity getAuthSetting(String name) {
        return oauthSettingRepository.findByName(name)
            .orElseThrow(() -> ExceptionUtils.mpe("Nonsupport %s login!", name));
    }

    public UserEntity getUserInfo(OAuthSettingEntity authSetting, String accessToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate
            .exchange(authSetting.getUrl() + authSetting.getUserInfoPath(), HttpMethod.GET, httpEntity,
                String.class);
        String userInfo = responseEntity.getBody();
        Assert.isFalse(StringUtils.isBlank(userInfo), "The user info must not be empty!");
        String email = getValue(userInfo, authSetting.getEmailKey());
        Assert.isTrue(StringUtils.isNotBlank(email), "The email must not empty!");
        UserEntity userEntity = UserEntity.builder().email(email).groupId(Initializer.DEFAULT_GROUP_ID)
            .username(getValue(userInfo, authSetting.getUsernameKey())).expiredDate(expiredDate)
            .source(authSetting.getName()).build();
        verifyUser(userEntity);
        return userEntity;
    }

    private String getValue(String userInfo, String key) {
        try {
            return JsonPath.read(userInfo, "$." + key);
        } catch (PathNotFoundException e) {
            log.error(e.getMessage());
        }
        return "";
    }

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
        body.set(REDIRECT_URI, oauthProperties.getRedirectUri(authSetting.getName()));
        body.set(CODE, code);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(authSetting.getClientId(), AesUtil.decrypt(authSetting.getClientSecret()));
        return new HttpEntity<>(body, httpHeaders);
    }

}
