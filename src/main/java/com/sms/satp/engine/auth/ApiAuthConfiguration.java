package com.sms.satp.engine.auth;

import io.restassured.spi.AuthFilter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({OnePlatformAuthConfig.class, ServiceMeshAuthConfig.class})

public class ApiAuthConfiguration {


    @Bean
    public ApiAuthManager apiAuthManager(List<AuthFilter> authFilters) {
        Map<AuthType, AuthFilter> authFilterMap = authFilters.stream().parallel().filter(
            authFilter -> authFilter.getClass().isAnnotationPresent(ApiAuth.class))
            .collect(Collectors.toMap(
                authFilter -> authFilter.getClass().getAnnotation(ApiAuth.class).type(),
                Function.identity()));

        return new ApiAuthManager(authFilterMap);
    }




}
