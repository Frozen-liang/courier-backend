package com.sms.satp.security.strategy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import java.time.Duration;
import javax.crypto.spec.SecretKeySpec;

public interface SatpSecurityStrategy {

    default SecretKeySpec generateSecretKeySpec(JwsHeader header, Claims claims) {
        SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
        return new SecretKeySpec(Decoders.BASE64.decode(generateSecretKey(claims)), alg.getJcaName());
    }

    String generateSecretKey(Claims claims);

    Duration obtainTokenExpirationTime();
}