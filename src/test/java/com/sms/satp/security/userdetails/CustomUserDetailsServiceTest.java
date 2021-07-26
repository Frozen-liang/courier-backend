package com.sms.satp.security.userdetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.dto.UserEntityAuthority;
import com.sms.satp.entity.system.SystemRoleEntity;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.service.UserService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsServiceTest {

    UserService userService = mock(UserService.class);
    UserDetailsService userDetailsService = new CustomUserDetailsService(userService);

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
        UserEntityAuthority userEntityAuthority = UserEntityAuthority.builder()
            .userEntity(UserEntity.builder().username(username).password(password).email(email).id(userId).build())
            .authorities(Collections.emptyList()).build();
        when(userService.getUserDetailsByUsernameOrEmail(username)).thenReturn(userEntityAuthority);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertThat(userDetails.getAuthorities().isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("Failed to findByUsernameOrEmailAndEnabledTrue")
    public void findByUsernameOrEmailAndEnabledTrueError() {
        when(userService.getUserDetailsByUsernameOrEmail(username)).thenThrow(UsernameNotFoundException.class);
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
            .isInstanceOf(UsernameNotFoundException.class);
    }
}