package com.sms.satp.security.userdetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.repository.UserGroupRepository;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.service.UserGroupService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest {

    UserRepository userRepository = mock(UserRepository.class);
    UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    UserGroupService userGroupService = mock(UserGroupService.class);

    UserDetailsService userDetailsService = new CustomUserDetailsService(userRepository, userGroupService);

    private String userId = "userId";
    private String username = "testUser";
    private String password = "password";
    private String email = "email";
    private String groupId = "group1";
    private List<String> roles = Arrays.asList("role1", "role2");
    private UserEntity userEntity = UserEntity.builder().id(userId)
        .username(username).password(password).email(email).groupId(groupId).build();
    private UserGroupEntity userGroupEntity = UserGroupEntity.builder().roleIds(roles).build();
    private Iterable<SystemRoleEntity> systemRoleEntities = Arrays.asList(
        SystemRoleEntity.builder().name("role1").build(),
        SystemRoleEntity.builder().name("role2").build());

    @Test
    @DisplayName("Load users by username under normal circumstances")
    public void normal() {
        when(userRepository.findByUsernameOrEmail(username, username))
            .thenReturn(Optional.of(userEntity));
        when(userGroupService.getAuthoritiesByUserGroup(groupId)).thenReturn(Collections.emptyList());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertThat(userDetails.getAuthorities().isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("Failed to findByUsernameOrEmailAndEnabledTrue")
    public void findByUsernameOrEmailAndEnabledTrueError() {
        when(userRepository.findByUsernameOrEmail(username, username))
            .thenReturn(Optional.empty());
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
            .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("Load users by username when the user's user group is empty")
    public void findUserGroupByIdError() {
        when(userRepository.findByUsernameOrEmail(username, username))
            .thenReturn(Optional.of(userEntity));
        when(userGroupRepository.findById(groupId)).thenReturn(Optional.empty());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertThat(userDetails.getAuthorities().isEmpty()).isEqualTo(true);
    }
}