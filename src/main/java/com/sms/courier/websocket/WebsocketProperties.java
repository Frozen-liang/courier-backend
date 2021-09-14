package com.sms.courier.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "courier.websocket")
public class WebsocketProperties {

    private String[] userAllowedOriginPatterns = {"*"};
    private String[] engineAllowedOriginPatterns = {"*"};
    private int messageSizeLimit = 655360;
}
