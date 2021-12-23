package com.sms.courier.engine.grpc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "courier.grpc.server")
public class GrpcProperties {
    private int port = 50051;
}
