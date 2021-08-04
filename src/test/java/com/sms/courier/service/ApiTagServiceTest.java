package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_TAG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_TAG_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_TAG_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_TAG_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.ApiTagType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiTagListRequest;
import com.sms.courier.dto.request.ApiTagRequest;
import com.sms.courier.dto.response.ApiTagResponse;
import com.sms.courier.entity.tag.ApiTagEntity;
import com.sms.courier.mapper.ApiTagMapper;
import com.sms.courier.mapper.ApiTagMapperImpl;
import com.sms.courier.repository.ApiTagRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.impl.ApiTagServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiTagService")
class ApiTagServiceTest {

    private final ApiTagRepository apiTagRepository = mock(ApiTagRepository.class);
    private final ApiTagMapper apiTagMapper = new ApiTagMapperImpl();
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ApiTagService apiTagService = new ApiTagServiceImpl(
        apiTagRepository, commonRepository, apiTagMapper);
    private final ApiTagEntity apiTag = ApiTagEntity.builder().id(ID).build();
    private final ApiTagResponse apiTagResponse = ApiTagResponse.builder().id(ID).build();
    private final ApiTagRequest apiTagRequest = ApiTagRequest.builder().id(ID).build();
    private final ApiTagListRequest apiTagListRequest = ApiTagListRequest.builder().projectId(PROJECT_ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the ApiTag service")
    public void findById_test() {
        when(apiTagRepository.findById(ID)).thenReturn(Optional.of(apiTag));
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
        when(apiTagRepository.insert(any(ApiTagEntity.class))).thenReturn(apiTag);
        apiTagService.add(apiTagRequest);
        verify(apiTagRepository, times(1)).insert(any(ApiTagEntity.class));
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTag")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).insert(any(ApiTagEntity.class));
        assertThatThrownBy(() -> apiTagService.add(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTag service")
    public void edit_test() {
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiTagRepository.save(any(ApiTagEntity.class))).thenReturn(apiTag);
        apiTagService.edit(apiTagRequest);
        verify(apiTagRepository, times(1)).save(any(ApiTagEntity.class));
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTag")
    public void edit_exception_test() {
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiTagRepository).save(any(ApiTagEntity.class));
        assertThatThrownBy(() -> apiTagService.edit(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TAG_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ApiTag")
    public void edit_not_exist_exception_test() {
        when(apiTagRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> apiTagService.edit(apiTagRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTag service")
    public void list_test() {
        ArrayList<ApiTagEntity> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            list.add(ApiTagEntity.builder().build());
        }
        when(apiTagRepository.findAll(any(), any(Sort.class))).thenReturn(list);
        List<ApiTagResponse> result = apiTagService.list(apiTagListRequest);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTag list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> apiTagService.list(apiTagListRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TAG_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTag service")
    public void delete_test() {
        when(apiTagRepository.findAllByIdIn(Collections.singletonList(ID)))
            .thenReturn(Stream.of(ApiTagEntity.builder().tagType(
                ApiTagType.API).id(ObjectId.get().toString()).build()));
        when(commonRepository.removeTags(any(), any(), any())).thenReturn(Boolean.TRUE);
        when(apiTagRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        assertThat(apiTagService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTag")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(apiTagRepository).findAllByIdIn(any());
        assertThatThrownBy(() -> apiTagService.delete(Collections.singletonList(ID)))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TAG_BY_ID_ERROR.getCode());
    }
}
