package com.sms.courier.service;

import com.sms.courier.dto.response.RoleResponse;
import com.sms.courier.entity.system.SystemRoleEntity;
import com.sms.courier.entity.system.UserGroupEntity;
import com.sms.courier.mapper.RoleMapper;
import com.sms.courier.mapper.RoleMapperImpl;
import com.sms.courier.repository.SystemRoleRepository;
import com.sms.courier.service.impl.RoleServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author zixi.gao
 * @create 2021-07-16 10:40
 */
@DisplayName("Tests for RoleService")
public class RoleServiceTest {

    private final SystemRoleRepository systemRoleRepository = mock(SystemRoleRepository.class);
    private final UserGroupService userGroupService = mock(UserGroupService.class);
    private final RoleMapper roleMapper = new RoleMapperImpl();
    private final RoleServiceImpl roleService = new RoleServiceImpl(systemRoleRepository, userGroupService, roleMapper);
    private final UserGroupEntity userGroupEntity = UserGroupEntity.builder().id(ID).build();

    private static final String ID = ObjectId.get().toString();
    private static final String GROUP_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the notFindRolesByGroupId method in the Role Service")
    public void notFindRolesByGroupId_test() {
        when(userGroupService.findById(ID)).thenReturn(userGroupEntity);
        when(systemRoleRepository.findAll()).thenReturn(List.of(SystemRoleEntity.builder().build()));
        List<RoleResponse> result = roleService.findRolesByGroupId(any());
        assertThat(result).allMatch((RoleResponse) -> !RoleResponse.isExist());
    }

    @Test
    @DisplayName("Test the findRolesByGroupId method in the Role Service")
    public void findRolesByGroupId_test() {
        String roleId = ObjectId.get().toString();
        when(userGroupService.findById(GROUP_ID))
            .thenReturn(UserGroupEntity.builder().roleIds(List.of(roleId)).build());
        when(systemRoleRepository.findAll()).thenReturn(List.of(SystemRoleEntity.builder().id(roleId).build()));
        List<RoleResponse> result = roleService.findRolesByGroupId(GROUP_ID);
        assertThat(result).allMatch(RoleResponse::isExist);
    }
}
