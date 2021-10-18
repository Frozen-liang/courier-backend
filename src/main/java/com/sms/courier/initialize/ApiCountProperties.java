package com.sms.courier.initialize;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "courier.api.count.reset")
public class ApiCountProperties {

    private Boolean caseCount;

    private Boolean sceneCaseCount;

}
