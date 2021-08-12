package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.courier.dto.response.RoleResponse;
import com.sms.courier.entity.system.SystemRoleEntity;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Tests for RoleMapperTest")
public class RoleMapperTest {

    private RoleMapper roleMapper = new RoleMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "project";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toResponse_Test(){
        SystemRoleEntity role = SystemRoleEntity.builder().build();
        RoleResponse dto = roleMapper.toResponse(role);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toResponse_IsNull_Test(){
        RoleResponse dto = roleMapper.toResponse(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDtoList_Test(){
        List<SystemRoleEntity> roleList = Lists.newArrayList(SystemRoleEntity.builder().build());
        assertThat(roleMapper.toDtoList(roleList)).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the Project's entity object to a dto object")
    void toDtoList_IsNull_Test(){
        List<SystemRoleEntity> roleList = null;
        assertThat(roleMapper.toDtoList(roleList)).isNull();
    }


}
