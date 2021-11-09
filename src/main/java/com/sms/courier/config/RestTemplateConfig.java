package com.sms.courier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private static final int CONNECT_TIME_OUT = 3 * 1000;
    private static final int REDA_TIME_OUT = 3 * 1000;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(CONNECT_TIME_OUT);
        httpRequestFactory.setReadTimeout(REDA_TIME_OUT);
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

}
