package com.sms.satp.security.jwt;

import com.sms.satp.security.AccessTokenProperties;
import com.sms.satp.security.pojo.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenManager implements InitializingBean {

    private static final String JWT_ISSUER = "x-api.com";
    public static final String API_AUTH = "Api-Auth";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    private final AccessTokenProperties accessTokenProperties;
    private SecretKey secretKey;

    public JwtTokenManager(AccessTokenProperties accessTokenProperties) {
        this.accessTokenProperties = accessTokenProperties;
    }

    public String generateAccessToken(CustomUser user) {
        return Jwts.builder()
            .setSubject(API_AUTH)
            .claim(USER_ID, user.getId())
            .claim(USERNAME, user.getUsername())
            .claim(EMAIL, user.getEmail())
            .setIssuer(JWT_ISSUER)
            .setIssuedAt(new Date())
            // 1 week
            .setExpiration(
                new Date(System.currentTimeMillis() + accessTokenProperties.getExpire().toMillis()))
            .signWith(secretKey)
            .compact();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody();

        return claims.get(USER_ID, String.class);
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get(USERNAME, String.class);
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get(EMAIL, String.class);
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey).build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature - {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token - {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token - {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token - {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty - {}", ex.getMessage());
        }
        return false;
    }


    @Override
    public void afterPropertiesSet() {

        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessTokenProperties.getSecretKey()));
    }

}
