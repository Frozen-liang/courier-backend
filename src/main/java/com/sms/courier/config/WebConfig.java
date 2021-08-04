package com.sms.courier.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConfigurationProperties(prefix = "api.cors")
@Data
public class WebConfig implements WebMvcConfigurer {

    private String[] origins = {"*"};
    private String[] methods = {"*"};
    private String mapping = "/**";
    private Long maxAge = 3600L;
    private Boolean allowCredentials = true;
    private final ObjectMapper objectMapper;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(mapping)
            .allowedOriginPatterns(origins)
            .allowedMethods(methods)
            .maxAge(maxAge)
            .allowCredentials(allowCredentials);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, new MappingJackson2HttpMessageConverter(objectMapper));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}