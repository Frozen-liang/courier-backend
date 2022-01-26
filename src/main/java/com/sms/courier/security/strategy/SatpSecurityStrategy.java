package com.sms.courier.security.strategy;

import io.jsonwebtoken.JwsHeader;
import java.security.Key;
import java.time.Duration;
import org.springframework.security.core.Authentication;

public interface SatpSecurityStrategy {

    Key generateSecretKey(JwsHeader<?> jwsHeader);

    Duration obtainTokenExpirationTime();

    Authentication createAuthentication(String id);
}