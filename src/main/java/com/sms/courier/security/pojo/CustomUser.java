package com.sms.courier.security.pojo;

import com.sms.courier.security.TokenType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CustomUser extends User {

    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private String nickname;
    private TokenType tokenType;
    private LocalDate expiredDate;


    private CustomUser(String id, String username, TokenType tokenType) {
        super(username, "", Collections.emptyList());
        this.id = id;
        this.tokenType = tokenType;
    }

    private CustomUser(String username, TokenType tokenType) {
        super(username, "", Collections.emptyList());
        this.tokenType = tokenType;
    }

    public CustomUser(String username, String password,
        Collection<? extends GrantedAuthority> authorities, String id, String email, String nickname,
        TokenType tokenType,
        LocalDate expiredDate) {
        super(username, password, authorities);
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.expiredDate = expiredDate;
        this.tokenType = tokenType;
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities, String id, String email, String nickname,
        TokenType tokenType,
        LocalDate expiredDate) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.tokenType = tokenType;
        this.expiredDate = expiredDate;
    }

    public CustomUser(UserDetails user, String id, String email, TokenType tokenType, LocalDate expiredDate) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.isAccountNonLocked(),
            user.getAuthorities());
        this.id = id;
        this.email = email;
        this.tokenType = tokenType;
        this.expiredDate = expiredDate;
    }

    public static CustomUser createEngine(String id) {
        return new CustomUser(id, "engine", TokenType.ENGINE);
    }

    public static CustomUser createUser(String id, String username) {
        return new CustomUser(id, username, TokenType.USER);
    }

    public static CustomUser createMock() {
        return new CustomUser("mock", TokenType.MOCK);
    }

    public static CustomUser createOpenApi(String id, String name) {
        return new CustomUser(id, name, TokenType.OPEN_API);
    }
}
