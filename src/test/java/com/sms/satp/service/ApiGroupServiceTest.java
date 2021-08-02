package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiGroupRequest;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.entity.group.ApiGroupEntity;
import com.sms.satp.mapper.ApiGroupMapper;
import com.sms.satp.repository.ApiGroupRepository;
import com.sms.satp.repository.CustomizedApiRepository;
import com.sms.satp.service.impl.ApiGroupServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test cases for ApiGroupServiceTest")
class ApiGroupServiceTest {

    private final ApiGroupRepository apiGroupRepository = mock(ApiGroupRepository.class);
    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ApiGroupMapper apiGroupMapper = mock(ApiGroupMapper.class);
    private final ApiGroupRequest apiGroupRequest = ApiGroupRequest.builder().build();
    private final ApiGroupEntity apiGroupEntityNotParent = ApiGroupEntity.builder().id(ID).projectId(ID)
        .name(PROJECT_NAME).parentId(null).build();
    private final ApiGroupEntity apiGroupEntityExistParent = ApiGroupEntity.builder().id(ID).projectId(ID)
        .name(PROJECT_NAME).parentId(ID).build();
    private final ApiGroupEntity apiGroupEntity = ApiGroupEntity.builder().id(ID).realGroupId(1L).projectId(ID)
        .name(PROJECT_NAME).parentId(ID).build();
    private final ApiGroupEntity parentGroup = ApiGroupEntity.builder().projectId(ID).build();
    private final ApiGroupService apiGroupService = new ApiGroupServiceImpl(apiGroupRepository, customizedApiRepository,
        apiGroupMapper);
    private final static String MOCK_ID = "1";
    private final static String ID = "1";
    private final static String PROJECT_NAME = "test";

    @Test
    @DisplayName("Test the list method in the ApiGroup service")
    void list_test() {
        when(apiGroupRepository.findApiGroupEntitiesByProjectId(any())).thenReturn(null);
        when(apiGroupMapper.toResponse(any()))
            .thenReturn(List.of(ApiGroupResponse.builder().id("1").depth(1).build(),
                ApiGroupResponse.builder().id("2").parentId("1").depth(2).build()));
        List<TreeResponse> dtoResponse = apiGroupService.list(MOCK_ID);
        assertThat(dtoResponse).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the ApiGroup service throw exception")
    void list_test_throwException() {
        when(apiGroupRepository.findApiGroupEntitiesByProjectId(any()))
            .thenThrow(new ApiTestPlatformException(GET_API_GROUP_LIST_ERROR));
        assertThatThrownBy(() -> apiGroupService.list(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the ApiGroup service")
    public void add_not_parent_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityNotParent);
        Boolean result = apiGroupService.add(apiGroupRequest);
        verify(apiGroupRepository, times(1)).insert(any(ApiGroupEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the add method in the ApiGroup service")
    public void add_exist_parent_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityExistParent);
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(parentGroup));
        Boolean result = apiGroupService.add(apiGroupRequest);
        verify(apiGroupRepository, times(1)).insert(any(ApiGroupEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the add method in the ApiGroup service throw exception")
    public void add_exist_depth_exception_test() {
        ApiGroupEntity apiGroupParentEntity = ApiGroupEntity.builder().depth(4).build();
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityExistParent);
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(apiGroupParentEntity));
        assertThatThrownBy(() -> apiGroupService.add(apiGroupRequest)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("message").isEqualTo("The group depth must be less than three.");
    }

    @Test
    @DisplayName("Test the add method in the ApiGroup service throw exception")
    public void add_exist_exception_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> apiGroupService.add(apiGroupRequest)).isInstanceOf(Exception.class).extracting("code")
            .isEqualTo(ADD_API_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiGroup service throw exception")
    public void add_id_not_exist_exception_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityExistParent);
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiGroupService.add(apiGroupRequest)).extracting("code")
            .isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());

    }

    @Test
    @DisplayName("Test the edit method in the ApiGroup service")
    public void edit_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityExistParent);
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(apiGroupEntityExistParent));
        Boolean result = apiGroupService.edit(apiGroupRequest);
        verify(apiGroupRepository, times(1)).save(any(ApiGroupEntity.class));
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the edit method in the ApiGroup service throw exception")
    public void edit_id_not_exist_exception_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenReturn(apiGroupEntityExistParent);
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiGroupService.edit(apiGroupRequest)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiGroup service throw exception")
    public void edit_exception_test() {
        when(apiGroupMapper.toEntity(apiGroupRequest)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> apiGroupService.edit(apiGroupRequest)).isInstanceOf(Exception.class).extracting("code")
            .isEqualTo(EDIT_API_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiGroup service")
    public void delete_success_test() {
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(apiGroupEntity));
        when(apiGroupRepository.findAllByPathContains(apiGroupEntity.getRealGroupId()))
            .thenReturn(Stream.of(ApiGroupEntity.builder().id(ID).build(),
                ApiGroupEntity.builder().id(ID).build()));
        doNothing().when(apiGroupRepository).deleteAllByIdIn(any());
        doNothing().when(customizedApiRepository).deleteByGroupIds(any());
        Boolean result = apiGroupService.delete(ID);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Test the delete method in the ApiGroup service")
    public void delete_fail_test() {
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(apiGroupEntity));
        when(apiGroupRepository.findAllByPathContains(apiGroupEntityExistParent.getRealGroupId()))
            .thenReturn(Stream.empty());
        assertThat(apiGroupService.delete(ID)).isFalse();
    }

    @Test
    @DisplayName("Test the delete method in the ApiGroup service")
    public void delete_test_when_realGroupId_isNull() {
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.of(ApiGroupEntity.builder().build()));
        doNothing().when(apiGroupRepository).deleteAllByIdIn(any());
        doNothing().when(customizedApiRepository).deleteByGroupIds(any());
        assertThat(apiGroupService.delete(ID)).isTrue();
    }

    @Test
    @DisplayName("Test the delete method in the ApiGroup service throw exception")
    public void delete_id_not_exist_exception_test() {
        when(apiGroupRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiGroupService.delete(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }


    @Test
    @DisplayName("Test the delete method in the ApiGroup service throw exception")
    public void delete_exception_test() {
        when(apiGroupRepository.findById(ID)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> apiGroupService.delete(ID)).isInstanceOf(Exception.class)
            .extracting("code").isEqualTo(DELETE_API_GROUP_BY_ID_ERROR.getCode());
    }

}
