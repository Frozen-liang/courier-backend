package com.sms.courier.config;

import static com.sms.courier.utils.EmailUtil.applyProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@EnableConfigurationProperties(EmailProperties.class)
@Configuration
public class EmailSenderConfiguration {

    @Bean
    JavaMailSenderImpl mailSender(EmailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        applyProperties(properties, sender);
        return sender;
    }
}