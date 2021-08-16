package com.sms.courier.security.userdetails;

import com.sms.courier.dto.UserEntityAuthority;
import com.sms.courier.security.TokenType;
import com.sms.courier.security.pojo.CustomUser;
import com.sms.courier.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntityAuthority userEntityAuthority = userService.getUserDetailsByUsernameOrEmail(username);

        UserDetails userDetails = User.withUsername(userEntityAuthority.getUserEntity().getUsername())
            .password(userEntityAuthority.getUserEntity().getPassword())
            .disabled(userEntityAuthority.getUserEntity().isRemoved()).accountExpired(false)
            .credentialsExpired(false).accountLocked(false).authorities(userEntityAuthority.getAuthorities()).build();

        return new CustomUser(userDetails, userEntityAuthority.getUserEntity().getId(),
            userEntityAuthority.getUserEntity().getEmail(), TokenType.USER, userEntityAuthority.getUserEntity()
            .getExpiredDate());
    }
}
