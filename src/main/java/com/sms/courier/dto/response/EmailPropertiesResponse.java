package com.sms.courier.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailPropertiesResponse {

    private String host;
    private Integer port;
    private String username;
    private String protocol;
    private String defaultEncoding;
    private Map<String, String> properties;
}