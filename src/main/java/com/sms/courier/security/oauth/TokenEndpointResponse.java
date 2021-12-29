package com.sms.courier.security.oauth;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenEndpointResponse {

    @JsonAlias("token_type")
    private String tokenType;
    @JsonAlias("expires_in")
    private Long expiresIn;
    @JsonAlias("access_token")
    private String accessToken;
    @JsonAlias("refresh_token")
    private String refreshToken;
    private String scope;
}
