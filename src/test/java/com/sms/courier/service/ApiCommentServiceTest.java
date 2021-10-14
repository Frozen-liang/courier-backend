package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_COMMENT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_COMMENT_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_COMMENT_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiCommentRequest;
import com.sms.courier.dto.response.ApiCommentResponse;
import com.sms.courier.entity.api.ApiCommentEntity;
import com.sms.courier.mapper.ApiCommentMapper;
import com.sms.courier.mapper.ApiCommentMapperImpl;
import com.sms.courier.repository.ApiCommentRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.impl.ApiCommentServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests for ApiCommentService")
class ApiCommentServiceTest {

    private final ApiCommentRepository apiCommentRepository = mock(ApiCommentRepository.class);
    private final CommonRepository commonRepository = mock(
        CommonRepository.class);
    private final ApiCommentMapper apiCommentMapper = new ApiCommentMapperImpl();
    private final ApiCommentService apiCommentService = new ApiCommentServiceImpl(
        apiCommentRepository, commonRepository, apiCommentMapper);
    private final ApiCommentEntity apiComment = ApiCommentEntity.builder().id(ID).build();
    private final ApiCommentRequest apiCommentRequest = ApiCommentRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final ObjectId API_ID = ObjectId.get();
    private static final ObjectId PARENT_ID = ObjectId.get();

    @Test
    @DisplayName("Test the findById method in the ApiComment service")
    public void findById_test() {
        when(apiCommentRepository.findById(ID)).thenReturn(Optional.of(apiComment));
        ApiCommentResponse result1 = apiCommentService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiComment")
    public void findById_exception_test() {
        when(apiCommentRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiCommentService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_COMMENT_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiComment service")
    public void add_test() {
        when(apiCommentRepository.insert(any(ApiCommentEntity.class))).thenReturn(apiComment);
        assertThat(apiCommentService.add(apiCommentRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ApiComment")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(apiCommentRepository).insert(any(ApiCommentEntity.class));
        assertThatThrownBy(() -> apiCommentService.add(apiCommentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_COMMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiComment service")
    public void edit_test() {
        when(apiCommentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiCommentRepository.save(any(ApiCommentEntity.class))).thenReturn(apiComment);
        assertThat(apiCommentService.edit(apiCommentRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ApiComment")
    public void edit_exception_test() {
        when(apiCommentRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiCommentRepository).save(any(ApiCommentEntity.class));
        assertThatThrownBy(() -> apiCommentService.edit(apiCommentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_COMMENT_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ApiComment")
    public void edit_not_exist_exception_test() {
        when(apiCommentRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> apiCommentService.edit(apiCommentRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiComment service")
    public void list_test() {
        ArrayList<ApiCommentResponse> apiCommentList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiCommentList.add(ApiCommentResponse.builder().build());
        }
        when(commonRepository.listLookupUser(anyString(), any(), any(Class.class))).thenReturn(apiCommentList);
        List<ApiCommentResponse> result = apiCommentService.list(API_ID, PARENT_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiComment list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(commonRepository).listLookupUser(anyString(), any(), any(Class.class));
        assertThatThrownBy(() -> apiCommentService.list(API_ID, PARENT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_COMMENT_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiComment service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(apiCommentRepository).deleteByIdIn(ids);
        assertThat(apiCommentService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiComment")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(apiCommentRepository).deleteByIdIn(ids);
        assertThatThrownBy(() -> apiCommentService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_COMMENT_BY_ID_ERROR.getCode());
    }

}
