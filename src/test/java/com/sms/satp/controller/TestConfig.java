package com.sms.satp.controller;

import static org.mockito.Mockito.mock;

import com.sms.satp.security.AccessTokenProperties;
import com.sms.satp.security.jwt.JwtTokenManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@TestConfiguration
public class TestConfig {

    @Bean
    public MappingMongoConverter mongoConverter() {
        return new MappingMongoConverter(mock(DbRefResolver.class), mock(MappingContext.class));
    }

    @Bean
    public JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager(accessTokenProperties());
    }

    @Bean
    public AccessTokenProperties accessTokenProperties() {
        AccessTokenProperties accessTokenProperties = new AccessTokenProperties();
        accessTokenProperties
            .setSecretKey("2U9TBPRTm6aVlxymkBqR255tLNKkGwGhBoYvJBNci+BvPaO/tnwEsFhxPEw7ESqG14icm6OyJwAAYeJB6XKKcw==");
        return accessTokenProperties;
    }

}
