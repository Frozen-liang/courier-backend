package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_USER_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_USER_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_USER_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_USER_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.UserGroupRequest;
import com.sms.satp.dto.response.UserGroupResponse;
import com.sms.satp.entity.system.UserGroupEntity;
import com.sms.satp.mapper.UserGroupMapper;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.repository.UserGroupRepository;
import com.sms.satp.service.impl.UserGroupServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for UserGroupService")
class UserGroupServiceTest {

    private final UserGroupRepository userGroupRepository = mock(UserGroupRepository.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(
        CommonDeleteRepository.class);
    private final UserGroupMapper userGroupMapper = mock(UserGroupMapper.class);
    private final UserGroupService userGroupService = new UserGroupServiceImpl(
        userGroupRepository, commonDeleteRepository, userGroupMapper);
    private final UserGroupEntity userGroup = UserGroupEntity.builder().id(ID).build();
    private final UserGroupResponse userGroupResponse = UserGroupResponse.builder()
        .id(ID).build();
    private final UserGroupRequest userGroupRequest = UserGroupRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;

    @Test
    @DisplayName("Test the findById method in the UserGroup service")
    public void findById_test() {
        when(userGroupRepository.findById(ID)).thenReturn(Optional.of(userGroup));
        when(userGroupMapper.toDto(userGroup)).thenReturn(userGroupResponse);
        UserGroupEntity result1 = userGroupService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }


    /*@Test
    @DisplayName("Test the add method in the UserGroup service")
    public void add_test() {
        when(userGroupMapper.toEntity(userGroupRequest)).thenReturn(userGroup);
        when(userGroupRepository.insert(any(UserGroupEntity.class))).thenReturn(userGroup);
        assertThat(userGroupService.add(userGroupRequest)).isTrue();
    }*/

    @Test
    @DisplayName("An exception occurred while adding UserGroup")
    public void add_exception_test() {
        when(userGroupMapper.toEntity(any())).thenReturn(UserGroupEntity.builder().build());
        doThrow(new RuntimeException()).when(userGroupRepository).insert(any(UserGroupEntity.class));
        assertThatThrownBy(() -> userGroupService.add(userGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_USER_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the UserGroup service")
    public void edit_test() {
        when(userGroupMapper.toEntity(userGroupRequest)).thenReturn(userGroup);
        when(userGroupRepository.findById(any())).thenReturn(Optional.of(userGroup));
        when(userGroupRepository.save(any(UserGroupEntity.class))).thenReturn(userGroup);
        assertThat(userGroupService.edit(userGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit UserGroup")
    public void edit_exception_test() {
        when(userGroupMapper.toEntity(userGroupRequest)).thenReturn(userGroup);
        when(userGroupRepository.findById(ID)).thenReturn(Optional.of(userGroup));
        doThrow(new RuntimeException()).when(userGroupRepository).save(any(UserGroupEntity.class));
        assertThatThrownBy(() -> userGroupService.edit(userGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_USER_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit UserGroup")
    public void edit_not_exist_exception_test() {
        when(userGroupMapper.toEntity(userGroupRequest)).thenReturn(userGroup);
        when(userGroupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userGroupService.edit(userGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the UserGroup service")
    public void list_test() {
        ArrayList<UserGroupEntity> userGroupList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            userGroupList.add(UserGroupEntity.builder().build());
        }
        ArrayList<UserGroupResponse> userGroupResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            userGroupResponseList.add(UserGroupResponse.builder().build());
        }
        when(userGroupRepository.findAllByRemovedIsFalseOrderByCreateDateTimeDesc()).thenReturn(userGroupList);
        when(userGroupMapper.toDtoList(userGroupList)).thenReturn(userGroupResponseList);
        List<UserGroupResponse> result = userGroupService.list();
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting UserGroup list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(userGroupRepository).findAllByRemovedIsFalseOrderByCreateDateTimeDesc();
        assertThatThrownBy(userGroupService::list)
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_USER_GROUP_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the UserGroup service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonDeleteRepository.deleteByIds(ids, UserGroupEntity.class)).thenReturn(Boolean.TRUE);
        assertThat(userGroupService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete UserGroup")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(commonDeleteRepository)
            .deleteByIds(ids, UserGroupEntity.class);
        assertThatThrownBy(() -> userGroupService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_USER_GROUP_BY_ID_ERROR.getCode());
    }

}