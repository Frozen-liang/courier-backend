package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_USER_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.UserRequest;
import com.sms.satp.dto.response.UserResponse;
import com.sms.satp.entity.system.UserEntity;
import com.sms.satp.mapper.UserMapper;
import com.sms.satp.mapper.UserMapperImpl;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.UserRepository;
import com.sms.satp.service.impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for UserService")
class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(
        CommonDeleteRepository.class);
    private final UserMapper userMapper = new UserMapperImpl();
    private final UserService userService = new UserServiceImpl(
        userRepository, commonDeleteRepository, userMapper);
    private final UserEntity user = UserEntity.builder().id(ID).build();
    private final UserRequest userRequest = UserRequest.builder().password("123Wac!@#")
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String GROUP_ID = ObjectId.get().toString();
    private static final String USERNAME = "test";

    @Test
    @DisplayName("Test the findById method in the User service")
    public void findById_test() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        UserResponse result1 = userService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting User")
    public void findById_exception_test() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_USER_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the User service")
    public void add_test() {
        when(userRepository.insert(any(UserEntity.class))).thenReturn(user);
        assertThat(userService.add(userRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding User")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(userRepository).insert(any(UserEntity.class));
        assertThatThrownBy(() -> userService.add(userRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_USER_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the User service")
    public void edit_test() {
        when(userRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        assertThat(userService.edit(userRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit User")
    public void edit_exception_test() {
        when(userRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(userRepository).save(any(UserEntity.class));
        assertThatThrownBy(() -> userService.edit(userRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_USER_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit User")
    public void edit_not_exist_exception_test() {
        when(userRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> userService.edit(userRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the User service")
    public void list_test() {
        ArrayList<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            userList.add(UserEntity.builder().build());
        }
        ArrayList<UserResponse> userResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            userResponseList.add(UserResponse.builder().build());
        }
        when(userRepository.findAll(any(), any(Sort.class))).thenReturn(userList);
        List<UserResponse> result = userService.list(USERNAME, GROUP_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting User list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(userRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> userService.list(USERNAME, GROUP_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_USER_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the User service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonDeleteRepository.deleteByIds(ids, UserEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(userService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete User")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(commonDeleteRepository)
            .deleteByIds(ids, UserEntity.class);
        assertThatThrownBy(() -> userService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_USER_BY_ID_ERROR.getCode());
    }

}