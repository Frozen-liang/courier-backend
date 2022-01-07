package com.sms.courier.security.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "courier.oauth")
public class OAuthProperties {

    private String redirectUri;

    public String getRedirectUri(OAuthType authType) {
        return redirectUri + "?type=" + authType.name();
    }
}
