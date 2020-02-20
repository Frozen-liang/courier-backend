package com.sms.satp.engine.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties("api.auth.service-mesh")
public class ServiceMeshAuthConfig {

    private String formAction = "/auth/token";
    private String userName;
    private String password;
    private boolean loggingEnabled = false;

}
