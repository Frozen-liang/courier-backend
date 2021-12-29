package com.sms.courier.utils;

import static com.sms.courier.common.constant.Constants.BEARER;

import com.sms.courier.security.pojo.CustomUser;
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
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

@Slf4j
public final class JwtUtils {

    private static final String JWT_ISSUER = "x-api.com";
    public static final String API_AUTH = "Api-Auth";

    public static final String TOKEN_USER_ID = "Id";
    public static final String TOKEN_TYPE = "Type";

    /**
     * * Convert specific io.jsonwebtoken.Jws object to encoded text with security key.
     *
     * @param customUser         io.jsonwebtoken.Jws instance which wraps JWT info.
     * @param signingKeyResolver Plain, textual security key.
     * @return Encoded JWT text.
     */
    public static Optional<String> encodeJwt(final CustomUser customUser,
        final SigningKeyResolver signingKeyResolver, final Duration expirationTime) {
        JwsHeader<?> jwsHeader = createJwsHeader(customUser);
        Objects.requireNonNull(customUser, "Missing JwtUtils.Jwt object.");
        Objects.requireNonNull(signingKeyResolver, "Missing secret key resolver.");
        try {
            JwtBuilder jwtBuilder = Jwts.builder().setHeaderParams(jwsHeader)
                .setSubject(API_AUTH)
                .setIssuer(JWT_ISSUER)
                .signWith(signingKeyResolver.resolveSigningKey(jwsHeader, new DefaultClaims()));
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
     * * Decode encoded JWT text and return JwsHeader instance.
     *
     * @param encodedJwtText     Encoded JWT text
     * @param signingKeyResolver Plain, textual security key.
     * @return io.jsonwebtoken.JwsHeader instance
     */
    public static JwsHeader<?> decodeJwt(final String encodedJwtText, final SigningKeyResolver signingKeyResolver) {
        Objects.requireNonNull(encodedJwtText, "Missing encoded JWT text.");
        Objects.requireNonNull(signingKeyResolver, "Missing secret key resolver.");
        return Jwts.parserBuilder().setSigningKeyResolver(signingKeyResolver)
            .build().parseClaimsJws(encodedJwtText).getHeader();
    }

    public static JwsHeader<?> createJwsHeader(CustomUser customUser) {
        JwsHeader<?> jwsHeader = new DefaultJwsHeader();
        jwsHeader.setAlgorithm(SignatureAlgorithm.HS256.getValue());
        jwsHeader.setType(Header.JWT_TYPE);
        jwsHeader.put(TOKEN_TYPE, customUser.getTokenType().name());
        jwsHeader.put(TOKEN_USER_ID, customUser.getId());
        return jwsHeader;
    }

    public static String getToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(header) && header.startsWith(BEARER)) {
            return header.split(" ")[1].trim();
        }
        return null;
    }

}
