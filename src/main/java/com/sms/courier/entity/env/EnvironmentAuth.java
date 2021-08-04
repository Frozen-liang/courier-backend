package com.sms.courier.entity.env;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EnvironmentAuth {

    private String status;
    private BasicAuth basicAuth;
    private JwtAuth jwtAuth;
}
