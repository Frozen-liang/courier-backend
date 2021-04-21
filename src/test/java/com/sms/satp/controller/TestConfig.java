package com.sms.satp.controller;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
@TestConfiguration
public class TestConfig {

        @Bean
        public MappingMongoConverter mongoConverter() {
            return new MappingMongoConverter(Mockito.mock(DbRefResolver.class), Mockito.mock(MappingContext.class));
        }

}
