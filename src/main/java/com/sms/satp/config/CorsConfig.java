package com.sms.satp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "api.cors")
@Data
public class CorsConfig implements WebMvcConfigurer {

    private String[] origins = {"*"};
    private String[] methods = {"*"};
    private String mapping = "/**";
    private Long maxAge = 3600L;
    private Boolean allowCredentials = true;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(mapping)
            .allowedOriginPatterns(origins)
            .allowedMethods(methods)
            .maxAge(maxAge)
            .allowCredentials(allowCredentials);
    }
}