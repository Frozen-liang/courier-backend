package com.sms.satp.security;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hive.security.access-token")
public class AccessTokenProperties {

    private String secretKey;
    private Duration expire = Duration.ofDays(7);
}
