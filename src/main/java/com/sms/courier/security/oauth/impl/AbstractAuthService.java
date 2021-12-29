package com.sms.courier.security.oauth.impl;

import static com.sms.courier.common.constant.Constants.BEARER;

import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.repository.AuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.oauth.AuthService;
import com.sms.courier.security.oauth.AuthSettingEntity;
import com.sms.courier.security.oauth.AuthType;
import com.sms.courier.security.oauth.TokenEndpointResponse;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.security.pojo.LoginResult;
import com.sms.courier.utils.ExceptionUtils;
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
import org.springframework.web.client.RestTemplate;

@Slf4j
public abstract class AbstractAuthService implements AuthService {

    protected final RestTemplate restTemplate;
    protected final AuthSettingRepository authSettingRepository;
    protected final UserRepository userRepository;
    protected final JwtTokenManager jwtTokenManager;
    private static final String TOKEN_URL = "%s/oauth2/token";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String GRANT_TYPE = "grant_type";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String SCOPE = "scope";
    private static final String CODE = "code";

    public AbstractAuthService(RestTemplate restTemplate,
        AuthSettingRepository authSettingRepository, UserRepository userRepository,
        JwtTokenManager jwtTokenManager) {
        this.restTemplate = restTemplate;
        this.authSettingRepository = authSettingRepository;
        this.userRepository = userRepository;
        this.jwtTokenManager = jwtTokenManager;
    }


    @Override
    public LoginResult createLoginResult(AuthType type, String code) {
        AuthSettingEntity authSetting = authSettingRepository.findByAuthType(type)
            .orElseThrow(() -> ExceptionUtils.mpe("Nonsupport %s login!", type));
        String url = String.format(TOKEN_URL, authSetting.getAuthUri());
        HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = createHttpEntity(code, authSetting);
        ResponseEntity<TokenEndpointResponse> responseEntity = restTemplate
            .postForEntity(url, httpEntity, TokenEndpointResponse.class);
        TokenEndpointResponse body = responseEntity.getBody();
        log.info("Body:{}", body);
        String header = getHeader(body);
        UserEntity userEntity = getUserInfo(authSetting.getAuthUri(), header);
        CustomUser user = CustomUser.createUser(userEntity.getId(), userEntity.getUsername());
        String token = jwtTokenManager.generateAccessToken(user);
        return LoginResult.builder().token(token).build();
    }

    public abstract UserEntity getUserInfo(String authUri, String accessToken);

    protected void verifyUser(UserEntity userEntity) {
        Optional<UserEntity> optional = userRepository.findByEmail(userEntity.getEmail());
        if (optional.isEmpty()) {
            if (userRepository.existsByUsername(userEntity.getUsername())) {
                userEntity.setUsername(RandomStringUtils.random(8));
            }
            userRepository.save(userEntity);
            return;
        }
        userEntity.setId(optional.get().getId());
    }

    private String getHeader(TokenEndpointResponse body) {
        if (Objects.isNull(body) || StringUtils.isBlank(body.getAccessToken())) {
            throw ExceptionUtils.mpe("Login fail!");
        }
        if (!BEARER.trim().equalsIgnoreCase(body.getTokenType())) {
            throw ExceptionUtils.mpe("Only support Bearer token type!");
        }
        return body.getAccessToken();
    }

    private HttpEntity<LinkedMultiValueMap<String, String>> createHttpEntity(String code,
        AuthSettingEntity authSetting) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.set(GRANT_TYPE, "authorization_code");
        // TODO set redirect_uri
        body.set(REDIRECT_URI, "");
        body.set(CODE, code);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(authSetting.getClientId(), authSetting.getClientSecret());
        return new HttpEntity<>(body, httpHeaders);
    }

}
