package com.sms.courier.dto.request;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

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
    private String defaultEncoding = StandardCharsets.UTF_8.name();
    @Builder.Default
    private Map<String, String> properties = new HashMap<>();
}
