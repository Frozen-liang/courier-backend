package com.sms.courier.dto.response;

import com.sms.courier.config.EmailProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailConfigurationResponse {

    private EmailProperties properties;
    private Boolean enabled;
}