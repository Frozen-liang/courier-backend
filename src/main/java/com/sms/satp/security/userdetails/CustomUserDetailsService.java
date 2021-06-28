package com.sms.satp.security.userdetails;

import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.repository.SystemRoleRepository;
import com.sms.satp.repository.UserGroupRepository;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.security.pojo.CustomUser;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final SystemRoleRepository systemRoleRepository;

    public CustomUserDetailsService(UserRepository userRepository,
        UserGroupRepository userGroupRepository, SystemRoleRepository systemRoleRepository) {
        this.userRepository = userRepository;
        this.userGroupRepository = userGroupRepository;
        this.systemRoleRepository = systemRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Query user info
        UserEntity userEntity =
            userRepository.findByUsernameOrEmailAndEnabledTrue(username, username)
                .orElseThrow(
                    () -> new UsernameNotFoundException(String.format("The user %s was not found.", username)));

        // Query user group
        UserGroupEntity userGroupEntity = userGroupRepository.findById(userEntity.getGroupId())
            .orElseThrow(() -> new UsernameNotFoundException(String.format("The user %s was not found.", username)));

        // Query permissions by group id
        Iterable<SystemRoleEntity> roles = systemRoleRepository.findAllById(userGroupEntity.getRoleIds());
        Collection<String> roleNames = CollectionUtils.collect(roles, SystemRoleEntity::getName);
        List<SimpleGrantedAuthority> authorities =
            CollectionUtils.isNotEmpty(roleNames) ? roleNames.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()) : Collections.emptyList();

        // Build UserDetails info of the Security
        UserDetails userDetails = User.withUsername(userEntity.getUsername()).password(userEntity.getPassword())
            .disabled(!userEntity.getEnabled()).accountExpired(false)
            .credentialsExpired(false).accountLocked(false).authorities(authorities).build();

        return new CustomUser(userDetails, userEntity.getId(),
            userEntity.getEmail());
    }
}
