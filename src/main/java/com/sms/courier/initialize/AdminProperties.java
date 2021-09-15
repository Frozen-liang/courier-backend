package com.sms.courier.initialize;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "courier.admin")
public class AdminProperties {

    private String username = "Admin";
    private String password;
}
