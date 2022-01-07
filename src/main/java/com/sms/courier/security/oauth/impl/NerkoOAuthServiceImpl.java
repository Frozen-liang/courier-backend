package com.sms.courier.security.oauth.impl;

import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.initialize.constant.Initializer;
import com.sms.courier.repository.OAuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
import com.sms.courier.security.oauth.OAuthProperties;
import com.sms.courier.security.oauth.OAuthType;
import com.sms.courier.security.oauth.model.NerkoUser;
import com.sms.courier.utils.Assert;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class NerkoOAuthServiceImpl extends AbstractOAuthService {

    public NerkoOAuthServiceImpl(RestTemplate restTemplate,
        OAuthSettingRepository oauthSettingRepository,
        UserRepository userRepository, JwtTokenManager jwtTokenManager, OAuthProperties oauthProperties) {
        super(restTemplate, oauthSettingRepository, userRepository, jwtTokenManager, oauthProperties);
    }

    @Override
    public UserEntity getUserInfo(String userInfoUri, OAuthType type, String accessToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<NerkoUser> responseEntity = restTemplate
            .exchange(userInfoUri, HttpMethod.GET, httpEntity, NerkoUser.class);
        NerkoUser nerkoUser = responseEntity.getBody();
        Assert.notNull(nerkoUser, "The user info must not be empty!");
        UserEntity userEntity = UserEntity.builder().email(nerkoUser.getEmail()).groupId(Initializer.DEFAULT_GROUP_ID)
            .username(nerkoUser.getUsername()).nickname(nerkoUser.getNickname()).expiredDate(expiredDate)
            .source(type.name()).build();
        verifyUser(userEntity);
        return userEntity;
    }

}
