package com.sms.courier.config;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "courier.mail")
public class EmailProperties {

    private static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

    @NotNull
    private String host;
    private Integer port;
    @NotNull
    private String username;
    @NotNull
    private String password;

    @Builder.Default
    private String protocol = "smtp";
    @Builder.Default
    private String defaultEncoding = DEFAULT_CHARSET;
    @Builder.Default
    private Map<String, String> properties = new HashMap<>();
}
