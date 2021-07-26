package com.sms.satp.security.userdetails;

import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.security.TokenType;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.UserGroupService;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserGroupService userGroupService;

    public CustomUserDetailsService(UserRepository userRepository,
        UserGroupService userGroupService) {
        this.userRepository = userRepository;
        this.userGroupService = userGroupService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Query user info
        UserEntity userEntity =
            userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(
                    () -> new UsernameNotFoundException(String.format("The user %s was not found.", username)));

        List<SimpleGrantedAuthority> authorities = userGroupService.getAuthoritiesByUserGroup(userEntity.getGroupId());

        // Build UserDetails info of the Security
        UserDetails userDetails = User.withUsername(userEntity.getUsername())
            .password(userEntity.getPassword())
            .disabled(userEntity.isRemoved()).accountExpired(false)
            .credentialsExpired(false).accountLocked(false).authorities(authorities).build();

        return new CustomUser(userDetails, userEntity.getId(), userEntity.getEmail(), TokenType.USER);
    }
}
