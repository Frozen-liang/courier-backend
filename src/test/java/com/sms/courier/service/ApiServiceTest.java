package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.BATCH_UPDATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_API_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.DocumentUrlType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiImportRequest;
import com.sms.courier.dto.request.ApiPageRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.BatchUpdateByIdRequest;
import com.sms.courier.dto.request.UpdateRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.ApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.api.ApiHistoryEntity;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import com.sms.courier.mapper.ApiHistoryMapper;
import com.sms.courier.mapper.ApiMapper;
import com.sms.courier.mapper.ApiMapperImpl;
import com.sms.courier.mapper.ParamInfoMapperImpl;
import com.sms.courier.repository.ApiDataStructureRefRecordRepository;
import com.sms.courier.repository.ApiHistoryRepository;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.impl.ApiServiceImpl;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;

@DisplayName("Tests for ApiService")
class ApiServiceTest {

    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiMapper apiMapper = new ApiMapperImpl(new ParamInfoMapperImpl());
    private final ApiHistoryRepository apiHistoryRepository = mock(ApiHistoryRepository.class);
    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ProjectImportSourceService projectImportSourceService = mock(ProjectImportSourceService.class);
    private final ApiHistoryMapper apiHistoryMapper = mock(ApiHistoryMapper.class);
    private final AsyncService asyncService = mock(AsyncService.class);
    private final ApiDataStructureRefRecordRepository apiDataStructureRefRecordRepository = mock(
        ApiDataStructureRefRecordRepository.class);
    private final ApiService apiService = new ApiServiceImpl(
        apiRepository, apiHistoryRepository, apiMapper, apiHistoryMapper, customizedApiRepository,
        apiDataStructureRefRecordRepository, asyncService, projectImportSourceService);
    private final ApiEntity api = ApiEntity.builder().id(ID).build();
    private final ApiResponse apiResponseDto = ApiResponse.builder().id(ID).build();
    private final ApiRequest apiRequestDto = ApiRequest.builder().id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private final BatchUpdateByIdRequest<Object> batchUpdateRequest = new BatchUpdateByIdRequest<>(List.of(ID),
        new UpdateRequest<>());

    @Test
    @DisplayName("Test the findById method in the Api service")
    public void findById_test() {
        when(customizedApiRepository.findById(ID)).thenReturn(Optional.of(apiResponseDto));
        ApiResponse result1 = apiService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);

    }

    @Test
    @DisplayName("An exception occurred while getting Api")
    public void findById_exception_test() {
        when(apiRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the Api service")
    public void add_test() {
        when(apiRepository.insert(any(ApiEntity.class))).thenReturn(api);
        when(apiHistoryRepository.insert(any(ApiHistoryEntity.class))).thenReturn(ApiHistoryEntity.builder().build());
        Boolean bool = apiService.add(apiRequestDto);
        assertThat(bool).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding Api")
    public void add_exception_test() {
        doThrow(new RuntimeException()).when(apiRepository).insert(any(ApiEntity.class));
        assertThatThrownBy(() -> apiService.add(apiRequestDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the Api service")
    public void edit_test() {
        when(apiRepository.findById(any())).thenReturn(Optional.of(api));
        when(apiRepository.save(any(ApiEntity.class))).thenReturn(api);
        when(apiHistoryRepository.insert(any(ApiHistoryEntity.class))).thenReturn(ApiHistoryEntity.builder().build());
        Boolean bool = apiService.edit(apiRequestDto);
        assertThat(bool).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit Api")
    public void edit_exception_test() {
        when(apiRepository.findById(any())).thenReturn(Optional.of(api));
        doThrow(new RuntimeException()).when(apiRepository).save(any(ApiEntity.class));
        assertThatThrownBy(() -> apiService.edit(apiRequestDto))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the page method in the Api service")
    public void page_test() {
        ApiPageRequest apiPageRequest = new ApiPageRequest();
        when(customizedApiRepository.page(apiPageRequest)).thenReturn(Page.empty());
        Page<ApiPageResponse> page = apiService.page(apiPageRequest);
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("An exception occurred while getting Api page")
    public void page_exception_test() {
        doThrow(new RuntimeException()).when(customizedApiRepository)
            .page(any());
        assertThatThrownBy(() -> apiService.page(new ApiPageRequest()))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_PAGE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the Api service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(customizedApiRepository.deleteByIds(ids)).thenReturn(Boolean.TRUE);
        assertThat(apiService.delete(ids)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete Api")
    public void delete_exception_test() {
        doThrow(new RuntimeException()).when(customizedApiRepository)
            .deleteByIds(Collections.singletonList(ID));
        assertThatThrownBy(() -> apiService.delete(Collections.singletonList(ID)))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the deleteByIds method in the Api service")
    public void deleteByIds_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(apiRepository).deleteAllByIdIn(ids);
        assertThat(apiService.deleteByIds(ids)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteAll method in the Api service")
    public void deleteAll_test() {
        doNothing().when(apiRepository).deleteAllByRemovedIsTrue();
        assertThat(apiService.deleteAll()).isTrue();
    }

    @Test
    @DisplayName("Test the importDocumentByFile method in the Api service")
    public void importDocumentByFile_test() throws IOException {
        ApiImportRequest apiImportRequest = mock(ApiImportRequest.class);
        byte[] bytes = {1, 2, 3, 4, 5, 6};
        MockMultipartFile file = new MockMultipartFile("test", bytes);
        when(apiImportRequest.getFile()).thenReturn(file);
        doNothing().when(asyncService).importApi(any());
        assertThat(apiService.importDocumentByFile(apiImportRequest)).isTrue();
    }

    @Test
    @DisplayName("Test the syncApiByProImpSourceIds method in the Api service")
    public void syncApiByProImpSourceIds_test() {
        List<String> proImpSourceIds = List.of(ObjectId.get().toString());
        when(projectImportSourceService.findByIds(any()))
            .thenReturn(
                List.of(ProjectImportSourceEntity.builder().documentType(DocumentUrlType.SWAGGER_FILE).build()));
        doNothing().when(asyncService).importApi(any());
        assertThat(apiService.syncApiByProImpSourceIds(proImpSourceIds)).isTrue();
    }

    @Test
    @DisplayName("Test for batchUpdateByIds in ApiService")
    public void batchUpdateByIds_test() {
        when(customizedApiRepository.updateFieldByIds(any(), any())).thenReturn(true);
        Boolean result = apiService.batchUpdateByIds(batchUpdateRequest);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while test batchUpdateByIds in ApiService.")
    public void batchUpdateByIds_exception_test() {
        when(customizedApiRepository.updateFieldByIds(any(), any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> apiService.batchUpdateByIds(batchUpdateRequest))
            .isInstanceOf(ApiTestPlatformException.class).extracting("code").isEqualTo(BATCH_UPDATE_ERROR.getCode());
    }
}