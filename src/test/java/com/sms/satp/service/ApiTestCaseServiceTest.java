package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EXECUTE_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_ENVIRONMENT_NOT_EXITS_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseExecuteRequest;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.request.DataCollectionRequest;
import com.sms.satp.dto.request.TestDataRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.ApiTestCaseJob;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.mapper.JobMapperImpl;
import com.sms.satp.repository.ApiTestCaseJobRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CommonDeleteRepository;
import com.sms.satp.service.impl.ApiTestCaseServiceImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiTestCaseService")
class ApiTestCaseServiceTest {

    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final CommonDeleteRepository commonDeleteRepository = mock(
        CommonDeleteRepository.class);
    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final JobMapper jobMapper = new JobMapperImpl();
    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final ApiTestCaseJobRepository apiTestCaseJobRepository = mock(ApiTestCaseJobRepository.class);
    private final ApiTestCaseService apiTestCaseService = new ApiTestCaseServiceImpl(
        apiTestCaseRepository, commonDeleteRepository, projectEnvironmentService,
        apiTestCaseJobRepository, apiTestCaseMapper, jobMapper);
    private final ApiTestCase apiTestCase = ApiTestCase.builder().id(ID).build();
    private final ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
        .id(ID).build();
    private final ApiTestCaseExecuteRequest apiTestCaseExecuteRequest =
        ApiTestCaseExecuteRequest.builder()
            .dataCollectionRequest(
                DataCollectionRequest.builder().dataList(Collections.singletonList(TestDataRequest.builder().build()))
                    .build())
            .build();
    private final ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
        .id(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String API_ID = ObjectId.get().toString();
    private static final String PROJECT_ID = ObjectId.get().toString();

    @Test
    @DisplayName("Test the findById method in the ApiTestCase service")
    public void findById_test() {
        when(apiTestCaseRepository.findById(ID)).thenReturn(Optional.of(apiTestCase));
        when(apiTestCaseMapper.toDto(apiTestCase)).thenReturn(apiTestCaseResponse);
        ApiTestCaseResponse result1 = apiTestCaseService.findById(ID);
        assertThat(result1).isNotNull();
        assertThat(result1.getId()).isEqualTo(ID);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCase")
    public void findById_exception_test() {
        when(apiTestCaseRepository.findById(ID)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> apiTestCaseService.findById(ID)).isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the add method in the ApiTestCase service")
    public void add_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.insert(any(ApiTestCase.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.add(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTestCase")
    public void add_exception_test() {
        when(apiTestCaseMapper.toEntity(any())).thenReturn(ApiTestCase.builder().build());
        doThrow(new RuntimeException()).when(apiTestCaseRepository).insert(any(ApiTestCase.class));
        assertThatThrownBy(() -> apiTestCaseService.add(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTestCase service")
    public void edit_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiTestCaseRepository.save(any(ApiTestCase.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.edit(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTestCase")
    public void edit_exception_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiTestCaseRepository).save(any(ApiTestCase.class));
        assertThatThrownBy(() -> apiTestCaseService.edit(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("An not exist exception occurred while edit ApiTestCase")
    public void edit_not_exist_exception_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.FALSE);
        assertThatThrownBy(() -> apiTestCaseService.edit(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EDIT_NOT_EXIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the list method in the ApiTestCase service")
    public void list_test() {
        ArrayList<ApiTestCase> apiTestCaseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTestCaseList.add(ApiTestCase.builder().build());
        }
        ArrayList<ApiTestCaseResponse> apiTestCaseResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTestCaseResponseList.add(ApiTestCaseResponse.builder().build());
        }
        when(apiTestCaseRepository.findAll(any(), any(Sort.class))).thenReturn(apiTestCaseList);
        when(apiTestCaseMapper.toDtoList(apiTestCaseList)).thenReturn(apiTestCaseResponseList);
        List<ApiTestCaseResponse> result = apiTestCaseService.list(API_ID, PROJECT_ID);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCase list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiTestCaseRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> apiTestCaseService.list(API_ID, PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTestCase service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(commonDeleteRepository.deleteByIds(ids, ApiTestCase.class)).thenReturn(Boolean.TRUE);
        assertThat(apiTestCaseService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTestCase")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(commonDeleteRepository)
            .deleteByIds(ids, ApiTestCase.class);
        assertThatThrownBy(() -> apiTestCaseService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the execute method in the ApiTestCase service")
    public void execute_test() {
        when(apiTestCaseRepository.findById(any())).thenReturn(Optional.of(apiTestCase));
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJob.class))).thenReturn(null);
        when(projectEnvironmentService.findOne(any())).thenReturn(ProjectEnvironment.builder().build());
        assertThat(apiTestCaseService.execute(apiTestCaseExecuteRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while execute ApiTestCase")
    public void environment_not_exist_exception_test() {
        when(apiTestCaseRepository.findById(any())).thenReturn(Optional.of(apiTestCase));
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJob.class))).thenReturn(null);
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        assertThatThrownBy(() -> apiTestCaseService.execute(apiTestCaseExecuteRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_ENVIRONMENT_NOT_EXITS_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while execute ApiTestCase")
    public void execute_exception_test() {
        when(apiTestCaseRepository.findById(any())).thenReturn(Optional.of(apiTestCase));
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJob.class))).thenReturn(null);
        when(projectEnvironmentService.findOne(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> apiTestCaseService.execute(apiTestCaseExecuteRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(EXECUTE_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("An exception occurred while execute ApiTestCase")
    public void api_test_case_not_exist_exception_test() {
        when(apiTestCaseRepository.findById(any())).thenReturn(Optional.empty());
        when(apiTestCaseJobRepository.insert(any(ApiTestCaseJob.class))).thenReturn(null);
        when(projectEnvironmentService.findOne(any())).thenReturn(ProjectEnvironment.builder().build());
        assertThatThrownBy(() -> apiTestCaseService.execute(apiTestCaseExecuteRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(THE_API_TEST_CASE_NOT_EXITS_ERROR.getCode());
    }
}