package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TAG_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TAG_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_GROUP_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_GROUP_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.entity.group.ApiTagGroup;
import com.sms.satp.mapper.ApiTagGroupMapper;
import com.sms.satp.repository.ApiTagGroupRepository;
import com.sms.satp.service.impl.ApiTagGroupServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

@DisplayName("Tests for ApiTagGroupService")
class ApiTagGroupServiceTest {

    private final ApiTagGroupRepository apiTagGroupRepository = mock(ApiTagGroupRepository.class);
    private final ApiTagGroupMapper apiTagGroupMapper = mock(ApiTagGroupMapper.class);
    private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
    private final ApiTagGroupService apiTagGroupService = new ApiTagGroupServiceImpl(
        apiTagGroupRepository, apiTagGroupMapper, mongoTemplate);
    private final ApiTagGroup apiTagGroup = ApiTagGroup.builder().id(ID).build();
    private final ApiTagGroupResponse apiTagGroupResponse = ApiTagGroupResponse.builder()
        .id(ID).build();
    private final ApiTagGroupRequest apiTagGroupRequest = ApiTagGroupRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String NOT_EXIST_ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the ApiTagGroup service")
    public void findById_test() {
        when(apiTagGroupRepository.findById(ID)).thenReturn(Optional.of(apiTagGroup));
        when(apiTagGroupMapper.toDto(apiTagGroup)).thenReturn(apiTagGroupResponse);
        ApiTagGroupResponse result1 = apiTagGroupService.findById(ID);
        ApiTagGroupResponse result2 = apiTagGroupService.findById(NOT_EXIST_ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
        assertThat(result2).isNull();
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTagGroup")
    public void findById_exception_test() {
        doThrow(new RuntimeException()).when(apiTagGroupRepository).findById(ID);
        assertThatThrownBy(() -> apiTagGroupService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_GROUP_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiTagGroup service")
    public void add_test() {
        when(apiTagGroupMapper.toEntity(apiTagGroupRequest)).thenReturn(apiTagGroup);
        when(apiTagGroupRepository.insert(any(ApiTagGroup.class))).thenReturn(apiTagGroup);
        assertThat(apiTagGroupService.add(apiTagGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTagGroup")
    public void add_exception_test() {
        when(apiTagGroupMapper.toEntity(any())).thenReturn(ApiTagGroup.builder().build());
        doThrow(new RuntimeException()).when(apiTagGroupRepository).insert(any(ApiTagGroup.class));
        assertThatThrownBy(() -> apiTagGroupService.add(apiTagGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TAG_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTagGroup service")
    public void edit_test() {
        when(apiTagGroupMapper.toEntity(apiTagGroupRequest)).thenReturn(apiTagGroup);
        when(apiTagGroupRepository.findById(any()))
            .thenReturn(Optional.of(ApiTagGroup.builder().id(ID).build()));
        when(apiTagGroupRepository.save(any(ApiTagGroup.class))).thenReturn(apiTagGroup);
        assertThat(apiTagGroupService.edit(apiTagGroupRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTagGroup")
    public void edit_exception_test() {
        when(apiTagGroupMapper.toEntity(apiTagGroupRequest)).thenReturn(apiTagGroup);
        when(apiTagGroupRepository.findById(any())).thenReturn(Optional.of(apiTagGroup));
        doThrow(new RuntimeException()).when(apiTagGroupRepository).save(any(ApiTagGroup.class));
        assertThatThrownBy(() -> apiTagGroupService.edit(apiTagGroupRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TAG_GROUP_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTagGroup service")
    public void list_test() {
        ArrayList<ApiTagGroup> apiTagGroupList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTagGroupList.add(ApiTagGroup.builder().build());
        }
        ArrayList<ApiTagGroupResponse> apiTagGroupResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTagGroupResponseList.add(ApiTagGroupResponse.builder().build());
        }
        when(apiTagGroupRepository.findByProjectId(any())).thenReturn(apiTagGroupList);
        when(apiTagGroupRepository.findByProjectIdAndName(any(), any())).thenReturn(Optional.empty());
        when(apiTagGroupMapper.toDtoList(apiTagGroupList)).thenReturn(apiTagGroupResponseList);
        List<ApiTagGroupResponse> result = apiTagGroupService.list(PROJECT_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTagGroup list")
    public void list_exception_test() {
        when(apiTagGroupRepository.findByProjectIdAndName(any(), any())).thenReturn(Optional.empty());
        doThrow(new RuntimeException()).when(apiTagGroupRepository).findByProjectId(any());
        assertThatThrownBy(() -> apiTagGroupService.list(PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_GROUP_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTagGroup service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(apiTagGroupRepository.deleteByIdIn(ids)).thenReturn(1L);
        assertThat(apiTagGroupService.delete(ids)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTagGroup")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(apiTagGroupRepository)
            .deleteByIdIn(ids);
        assertThatThrownBy(() -> apiTagGroupService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TAG_GROUP_BY_ID_ERROR.getCode());
    }
}