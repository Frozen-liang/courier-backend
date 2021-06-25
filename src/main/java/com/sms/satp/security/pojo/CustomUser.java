package com.sms.satp.security.pojo;

import java.util.Collection;
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

    public CustomUser(String username, String password,
        Collection<? extends GrantedAuthority> authorities, String id, String email) {
        super(username, password, authorities);
        this.id = id;
        this.email = email;
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
        boolean credentialsNonExpired, boolean accountNonLocked,
        Collection<? extends GrantedAuthority> authorities, String id, String email) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.email = email;
    }

    public CustomUser(UserDetails user, String id, String email) {
        super(user.getUsername(), user.getPassword(), user.isEnabled(), user.isAccountNonExpired(),
            user.isCredentialsNonExpired(),
            user.isAccountNonLocked(),
            user.getAuthorities());
        this.id = id;
        this.email = email;
    }
}
