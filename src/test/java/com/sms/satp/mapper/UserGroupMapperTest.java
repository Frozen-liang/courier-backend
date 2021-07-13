package com.sms.satp.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.sms.satp.dto.request.UserGroupRequest;
import com.sms.satp.dto.response.UserGroupResponse;
import com.sms.satp.entity.system.UserGroupEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserGroupMapper")
class UserGroupMapperTest {

    private UserGroupMapper userGroupMapper = new UserGroupMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "userGroup";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the UserGroup's entity object to a dto object")
    void entity_to_dto() {
        UserGroupEntity userGroup = UserGroupEntity.builder()
            .name(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        UserGroupResponse userGroupResponse = userGroupMapper.toDto(userGroup);
        assertThat(userGroupResponse.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an UserGroup entity list object to a dto list object")
    void userGroupList_to_userGroupDtoList() {
        List<UserGroupEntity> userGroups = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            userGroups.add(UserGroupEntity.builder().name(NAME).build());
        }
        List<UserGroupResponse> userGroupResponseList = userGroupMapper.toDtoList(userGroups);
        assertThat(userGroupResponseList).hasSize(SIZE);
        assertThat(userGroupResponseList).allMatch(result -> StringUtils.equals(result.getName(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the UserGroup's dto object to a entity object")
    void dto_to_entity() {
        UserGroupRequest userGroupRequest = UserGroupRequest.builder()
            .name(NAME)
            .build();
        UserGroupEntity userGroup = userGroupMapper.toEntity(userGroupRequest);
        assertThat(userGroup.getName()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the UserGroup's entity object to a dto object")
    void null_entity_to_dto() {
        UserGroupResponse userGroupResponse = userGroupMapper.toDto(null);
        assertThat(userGroupResponse).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the UserGroup's dto object to a entity object")
    void null_dto_to_entity() {
        UserGroupEntity userGroup = userGroupMapper.toEntity(null);
        assertThat(userGroup).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an UserGroup entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<UserGroupResponse> userGroupDtoList = userGroupMapper.toDtoList(null);
        assertThat(userGroupDtoList).isNull();
    }

}