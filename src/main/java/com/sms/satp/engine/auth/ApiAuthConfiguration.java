package com.sms.satp.engine.auth;

import com.sms.satp.engine.auth.filter.OnePlatformAuthFilter;
import com.sms.satp.engine.auth.filter.ServiceMeshAuthFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties({OnePlatformAuthConfig.class, ServiceMeshAuthConfig.class})
public class ApiAuthConfiguration {


    @Bean
    public ServiceMeshAuthFilter serviceMeshAuthFilter(ServiceMeshAuthConfig serviceMeshAuthConfig) {
        return new ServiceMeshAuthFilter(serviceMeshAuthConfig);
    }

    @Bean
    public OnePlatformAuthFilter onePlatformAuthFilter(OnePlatformAuthConfig onePlatformAuthConfig) {
        return new OnePlatformAuthFilter(onePlatformAuthConfig);
    }

}
