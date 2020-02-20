package com.sms.satp.engine.auth;


import io.restassured.spi.AuthFilter;
import java.util.Map;

public class ApiAuthManager {

    private final Map<AuthType, AuthFilter> authFilters;


    public ApiAuthManager(Map<AuthType, AuthFilter> authFilters) {
        this.authFilters = authFilters;
    }

    public AuthFilter getAuthFilter(AuthType authType) {
        return authFilters.get(authType);
    }
}
