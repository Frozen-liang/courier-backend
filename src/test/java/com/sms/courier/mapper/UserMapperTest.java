package com.sms.courier.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.dto.request.UserQueryListRequest;
import com.sms.courier.dto.request.UserRequest;
import com.sms.courier.dto.response.UserProfileResponse;
import com.sms.courier.dto.response.UserResponse;
import com.sms.courier.entity.system.UserEntity;
import com.sms.courier.security.pojo.CustomUser;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserMapper")
class UserMapperTest {

    private UserMapper userMapper = new UserMapperImpl();

    private static final Integer SIZE = 10;
    private static final String NAME = "username";
    private static final LocalDateTime CREATE_TIME = LocalDateTime.now();
    private static final LocalDateTime MODIFY_TIME = LocalDateTime.now();

    @Test
    @DisplayName("Test the method to convert the User's entity object to a dto object")
    void entity_to_dto() {
        UserEntity user = UserEntity.builder()
            .username(NAME)
            .createDateTime(CREATE_TIME)
            .modifyDateTime(MODIFY_TIME)
            .build();
        UserResponse userResponse = userMapper.toDto(user);
        assertThat(userResponse.getUsername()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("Test the method for converting an User entity list object to a dto list object")
    void userList_to_userDtoList() {
        List<UserEntity> users = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            users.add(UserEntity.builder().username(NAME).build());
        }
        List<UserResponse> userResponseList = userMapper.toDtoList(users);
        assertThat(userResponseList).hasSize(SIZE);
        assertThat(userResponseList).allMatch(result -> StringUtils.equals(result.getUsername(), NAME));
    }

    @Test
    @DisplayName("Test the method to convert the User's dto object to a entity object")
    void dto_to_entity() {
        UserRequest userRequest = UserRequest.builder()
            .username(NAME)
            .build();
        UserEntity user = userMapper.toEntity(userRequest);
        assertThat(user.getUsername()).isEqualTo(NAME);
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the User's entity object to a dto object")
    void null_entity_to_dto() {
        UserResponse userResponse = userMapper.toDto(null);
        assertThat(userResponse).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method for converting an User entity list object to a dto list object")
    void null_entityList_to_dtoList() {
        List<UserResponse> userDtoList = userMapper.toDtoList(null);
        assertThat(userDtoList).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the User's entity object to a dto object")
    void toEntity_IsNull_Test() {
        UserRequest userRequest = null;
        UserEntity dto = userMapper.toEntity(userRequest);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("[Null Input Parameter]Test the method to convert the User's entity object to a dto object")
    void toEntity_to_IsNull_Test() {
        UserQueryListRequest userRequest = null;
        UserEntity dto = userMapper.toEntity(userRequest);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Test the method to convert the User's entity object to a dto object")
    void toUserResponse_Test() {
        CustomUser customUser = mock(CustomUser.class);
        when(customUser.getAuthorities()).thenReturn(Lists.newArrayList());
        UserProfileResponse dto = userMapper.toUserResponse(customUser);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the method to convert the User's entity object to a dto object")
    void toUserResponse_IsNull_Test() {
        UserProfileResponse dto = userMapper.toUserResponse(null);
        assertThat(dto).isNull();
    }

}