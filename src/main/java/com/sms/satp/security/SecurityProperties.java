package com.sms.satp.security;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "hive.security")
public class SecurityProperties {

    private List<String> ignorePath = new ArrayList<>();

}
