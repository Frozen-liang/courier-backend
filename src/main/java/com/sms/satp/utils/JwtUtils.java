package com.sms.satp.utils;

import com.sms.satp.security.pojo.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
public final class JwtUtils {

    private static final String JWT_ISSUER = "x-api.com";
    public static final String API_AUTH = "Api-Auth";
    public static final String TOKEN_EMAIL = "email";
    public static final String TOKEN_USERNAME = "username";
    public static final String TOKEN_USER_ID = "userId";
    public static final String TOKEN_AUTHORITIES = "authorities";
    public static final String TOKEN_TYPE = "tokenType";

    /**
     * * Convert specific io.jsonwebtoken.Jws object to encoded text with security key.
     *
     * @param customUser         io.jsonwebtoken.Jws instance which wraps JWT info.
     * @param signingKeyResolver Plain, textual security key.
     * @return Encoded JWT text.
     */
    public static Optional<String> encodeJwt(final CustomUser customUser,
        final SigningKeyResolver signingKeyResolver, final Duration expirationTime) {
        Map<String, Object> claims = covertToClaim(customUser);
        JwsHeader<?> jwsHeader = createJwsHeader();
        Objects.requireNonNull(customUser, "Missing JwtUtils.Jwt object.");
        Objects.requireNonNull(signingKeyResolver, "Missing secret key resolver.");
        try {
            JwtBuilder jwtBuilder = Jwts.builder().setHeaderParams(jwsHeader)
                .setSubject(API_AUTH)
                .setClaims(claims)
                .setIssuer(JWT_ISSUER)
                .signWith(signingKeyResolver.resolveSigningKey(jwsHeader, new DefaultClaims(claims)));
            if (Objects.nonNull(expirationTime) && expirationTime.compareTo(Duration.ZERO) > 0L) {
                jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expirationTime.toMillis()));
            }
            String encodeJwtText = jwtBuilder.compact();
            return Optional.ofNullable(encodeJwtText);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
    }


    /**
     * * Decode encoded JWT text and return JWT instance.
     *
     * @param encodedJwtText     Encoded JWT text
     * @param signingKeyResolver Plain, textual security key.
     * @return io.jsonwebtoken.Claims instance
     */
    public static Claims decodeJwt(final String encodedJwtText, final SigningKeyResolver signingKeyResolver) {
        Objects.requireNonNull(encodedJwtText, "Missing encoded JWT text.");
        Objects.requireNonNull(signingKeyResolver, "Missing secret key resolver.");
        return Jwts.parserBuilder().setSigningKeyResolver(signingKeyResolver)
            .build().parseClaimsJws(encodedJwtText).getBody();
    }


    public static Map<String, Object> covertToClaim(CustomUser customUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_EMAIL, customUser.getEmail());
        claims.put(TOKEN_USERNAME, customUser.getUsername());
        claims.put(TOKEN_USER_ID, customUser.getId());
        claims.put(TOKEN_AUTHORITIES, customUser.getAuthorities().stream().map(
            GrantedAuthority::getAuthority).collect(Collectors.toList()));
        claims.put(TOKEN_TYPE, customUser.getTokenType().name());
        return claims;
    }

    public static JwsHeader<?> createJwsHeader() {
        JwsHeader<?> jwsHeader = new DefaultJwsHeader();
        jwsHeader.setAlgorithm(SignatureAlgorithm.HS256.getValue());
        jwsHeader.setType(Header.JWT_TYPE);
        return jwsHeader;
    }

}
