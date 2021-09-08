package com.sms.courier.utils;

import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import java.time.LocalDate;
import java.util.Collection;
import java.util.function.Consumer;
import javax.security.auth.login.AccountNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private static final String ADMIN = "ADMIN";

    public static String getCurrUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validUser(principal);
        CustomUser user = (CustomUser) principal;
        return user.getId();
    }

    public static CustomUser getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        validUser(principal);
        return (CustomUser) principal;
    }

    private static void validUser(Object principal) {
        if ("anonymousUser".equals(principal.toString())) {
            throw new RuntimeException(new AccountNotFoundException());
        }
    }

    public static void fillWithCurrentUser(Consumer<CustomUser> callback) {
        CustomUser currentUser = getCurrentUser();
        callback.accept(currentUser);
    }

    public static boolean isSuperAdmin(Collection<? extends GrantedAuthority> roles) {

        return roles.stream()
            .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase(ADMIN));
    }

    public static boolean isSuperAdmin() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities();
        return authorities.stream()
            .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase(ADMIN));
    }

    public static CustomUser getCustomUser(Authentication authentication) {
        return (CustomUser) authentication.getPrincipal();
    }

    public static Authentication newAuthentication(String userId, String email, String username, String nickname,
        Collection<? extends GrantedAuthority> authorities, TokenType tokenType, LocalDate expiredDate) {
        CustomUser customUser = new CustomUser(username, "",
            true, true, true, true,
            authorities, userId, email, nickname, tokenType, expiredDate);
        return new UsernamePasswordAuthenticationToken(customUser, null, authorities);
    }

}