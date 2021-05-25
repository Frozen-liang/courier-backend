package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TAG_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTagRequest;
import com.sms.satp.dto.response.ApiTagResponse;
import com.sms.satp.entity.tag.ApiTag;
import com.sms.satp.mapper.ApiTagMapper;
import com.sms.satp.repository.ApiTagRepository;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.service.impl.ApiTagServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiTagService")
class ApiTagServiceTest {

    private final ApiTagRepository apiTagRepository = mock(ApiTagRepository.class);
    private final ApiTagMapper apiTagMapper = mock(ApiTagMapper.class);
    private final ApiTagService apiTagService = new ApiTagServiceImpl(
        apiTagRepository, apiTagMapper);
    private final ApiTag apiTag = ApiTag.builder().id(ID).build();
    private final ApiTagResponse apiTagResponse = ApiTagResponse.builder().id(ID).build();
    private final ApiTagRequest apiTagRequest = ApiTagRequest.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = "10";
    private static final String TAG_NAME = "testName";
    private static final Integer TAG_TYPE = 1;

    @Test
    @DisplayName("Test the findById method in the ApiTag service")
    public void findById_test() {
        when(apiTagRepository.findById(ID)).thenReturn(Optional.of(apiTag));
        when(apiTagMapper.toDto(apiTag)).thenReturn(apiTagResponse);
        ApiTagResponse result1 = apiTagService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTag")
    public void findById_exception_test() {
        when(apiTagRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiTagService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiTag service")
    public void add_test() {
        when(apiTagMapper.toEntity(apiTagRequest)).thenReturn(apiTag);
        when(apiTagRepository.insert(any(ApiTag.class))).thenReturn(apiTag);
        apiTagService.add(apiTagRequest);
        verify(apiTagRepository, times(1)).insert(any(ApiTag.class));
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTag")
    public void add_exception_test() {
        when(apiTagMapper.toEntity(apiTagRequest)).thenReturn(apiTag);
        doThrow(new RuntimeException()).when(apiTagRepository).insert(any(ApiTag.class));
        assertThatThrownBy(() -> apiTagService.add(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTag service")
    public void edit_test() {
        when(apiTagMapper.toEntity(apiTagRequest)).thenReturn(apiTag);
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiTagRepository.save(any(ApiTag.class))).thenReturn(apiTag);
        apiTagService.edit(apiTagRequest);
        verify(apiTagRepository, times(1)).save(any(ApiTag.class));
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTag")
    public void edit_exception_test() {
        when(apiTagMapper.toEntity(apiTagRequest)).thenReturn(apiTag);
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiTagRepository).save(any(ApiTag.class));
        assertThatThrownBy(() -> apiTagService.edit(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ApiTag")
    public void edit_not_exist_exception_test() {
        when(apiTagMapper.toEntity(apiTagRequest)).thenReturn(apiTag);
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> apiTagService.edit(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTag service")
    public void list_test() {
        ArrayList<ApiTag> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ApiTag.builder().build());
        }
        ArrayList<ApiTagResponse> apiTagDtos = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTagDtos.add(ApiTagResponse.builder().build());
        }
        when(apiTagRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        when(apiTagMapper.toDtoList(list)).thenReturn(apiTagDtos);
        List<ApiTagResponse> result = apiTagService.list(PROJECT_ID, TAG_NAME, TAG_TYPE);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTag list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> apiTagService.list(PROJECT_ID, TAG_NAME, TAG_TYPE))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTag service")
    public void delete_test() {
        when(apiTagRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        assertThat(apiTagService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTag")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> apiTagService.delete(Collections.singletonList(ID)))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TAG_BY_ID_ERROR.getCode());
    }
}
