package com.sms.courier.security.oauth.impl;

import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.repository.AuthSettingRepository;
import com.sms.courier.repository.UserRepository;
import com.sms.courier.security.jwt.JwtTokenManager;
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
public class NerkoAuthServiceImpl extends AbstractAuthService {

    private static final String USER_INFO_URL = "%s/userinfo";

    public NerkoAuthServiceImpl(RestTemplate restTemplate,
        AuthSettingRepository authSettingRepository,
        UserRepository userRepository,
        JwtTokenManager jwtTokenManager) {
        super(restTemplate, authSettingRepository, userRepository, jwtTokenManager);
    }

    @Override
    public UserEntity getUserInfo(String authUri, String token) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<NerkoUser> responseEntity = restTemplate
            .exchange(String.format(USER_INFO_URL, authUri), HttpMethod.GET, httpEntity, NerkoUser.class);
        NerkoUser nerkoUser = responseEntity.getBody();
        Assert.notNull(nerkoUser, "The user info must not be empty!");
        UserEntity userEntity = UserEntity.builder().email(nerkoUser.getEmail())
            .username(nerkoUser.getUsername()).nickname(nerkoUser.getNickname()).build();
        verifyUser(userEntity);
        return userEntity;
    }
}
