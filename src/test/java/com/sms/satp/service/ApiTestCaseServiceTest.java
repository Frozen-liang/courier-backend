package com.sms.satp.service;

import static com.sms.satp.common.exception.ErrorCode.ADD_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_API_TEST_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_API_TEST_CASE_LIST_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.response.ApiTestCaseResponse;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.JobMapper;
import com.sms.satp.mapper.JobMapperImpl;
import com.sms.satp.mapper.MatchParamInfoMapperImpl;
import com.sms.satp.mapper.ParamInfoMapper;
import com.sms.satp.mapper.ParamInfoMapperImpl;
import com.sms.satp.mapper.ResponseResultVerificationMapperImpl;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CustomizedApiTestCaseJobRepository;
import com.sms.satp.repository.CustomizedApiTestCaseRepository;
import com.sms.satp.security.pojo.CustomUser;
import com.sms.satp.service.impl.ApiTestCaseServiceImpl;
import com.sms.satp.utils.SecurityUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;

@DisplayName("Tests for ApiTestCaseService")
class ApiTestCaseServiceTest {

    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository = mock(
        CustomizedApiTestCaseRepository.class);
    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));

    private final CustomizedApiTestCaseJobRepository customizedApiTestCaseJobRepository = mock(
        CustomizedApiTestCaseJobRepository.class);
    private final ApiTestCaseService apiTestCaseService = new ApiTestCaseServiceImpl(
        apiTestCaseRepository, customizedApiTestCaseRepository, customizedApiTestCaseJobRepository, apiTestCaseMapper,
        jobMapper);
    private final ApiTestCaseEntity apiTestCase = ApiTestCaseEntity.builder().id(ID).build();
    private final ApiTestCaseResponse apiTestCaseResponse = ApiTestCaseResponse.builder()
        .id(ID).build();
    private final ApiTestCaseRequest apiTestCaseRequest = ApiTestCaseRequest.builder()
        .id(ID).build();
    private static final boolean REMOVED = Boolean.FALSE;
    private static final String ID = ObjectId.get().toString();
    private static final Integer TOTAL_ELEMENTS = 10;
    private static final String API_ID = ObjectId.get().toString();
    private static final String PROJECT_ID = ObjectId.get().toString();
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;

    static {
        securityUtilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    public static void close() {
        securityUtilMockedStatic.close();
    }

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
        CustomUser customUser = mock(CustomUser.class);
        securityUtilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(customUser);
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.insert(any(ApiTestCaseEntity.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.add(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while adding ApiTestCase")
    public void add_exception_test() {
        when(apiTestCaseMapper.toEntity(any())).thenReturn(ApiTestCaseEntity.builder().build());
        doThrow(new RuntimeException()).when(apiTestCaseRepository).insert(any(ApiTestCaseEntity.class));
        assertThatThrownBy(() -> apiTestCaseService.add(apiTestCaseRequest))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(ADD_API_TEST_CASE_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the edit method in the ApiTestCase service")
    public void edit_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(apiTestCaseRepository.save(any(ApiTestCaseEntity.class))).thenReturn(apiTestCase);
        assertThat(apiTestCaseService.edit(apiTestCaseRequest)).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while edit ApiTestCase")
    public void edit_exception_test() {
        when(apiTestCaseMapper.toEntity(apiTestCaseRequest)).thenReturn(apiTestCase);
        when(apiTestCaseRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doThrow(new RuntimeException()).when(apiTestCaseRepository).save(any(ApiTestCaseEntity.class));
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
        ArrayList<ApiTestCaseEntity> apiTestCaseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTestCaseList.add(ApiTestCaseEntity.builder().build());
        }
        ArrayList<ApiTestCaseResponse> apiTestCaseResponseList = new ArrayList<>();
        for (int i = 0; i < TOTAL_ELEMENTS; i++) {
            apiTestCaseResponseList.add(ApiTestCaseResponse.builder().build());
        }
        when(apiTestCaseRepository.findAll(any(), any(Sort.class))).thenReturn(apiTestCaseList);
        when(apiTestCaseMapper.toDtoList(apiTestCaseList)).thenReturn(apiTestCaseResponseList);
        when(customizedApiTestCaseJobRepository.findRecentlyCaseReportByCaseId(any()))
            .thenReturn(ApiTestCaseJobEntity.builder().build());
        List<ApiTestCaseResponse> result = apiTestCaseService.list(API_ID, PROJECT_ID, REMOVED);
        assertThat(result).hasSize(TOTAL_ELEMENTS);
    }

    @Test
    @DisplayName("An exception occurred while getting ApiTestCase list")
    public void list_exception_test() {
        doThrow(new RuntimeException()).when(apiTestCaseRepository).findAll(any(), any(Sort.class));
        assertThatThrownBy(() -> apiTestCaseService.list(API_ID, PROJECT_ID, REMOVED))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(GET_API_TEST_CASE_LIST_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the delete method in the ApiTestCase service")
    public void delete_test() {
        List<String> ids = Collections.singletonList(ID);
        when(customizedApiTestCaseRepository.deleteByIds(ids)).thenReturn(Boolean.TRUE);
        assertThat(apiTestCaseService.delete(Collections.singletonList(ID))).isTrue();
    }

    @Test
    @DisplayName("An exception occurred while delete ApiTestCase")
    public void delete_exception_test() {
        List<String> ids = Collections.singletonList(ID);
        doThrow(new RuntimeException()).when(customizedApiTestCaseRepository)
            .deleteByIds(ids);
        assertThatThrownBy(() -> apiTestCaseService.delete(ids))
            .isInstanceOf(ApiTestPlatformException.class)
            .extracting("code").isEqualTo(DELETE_API_TEST_CASE_BY_ID_ERROR.getCode());
    }

    @Test
    @DisplayName("Test the updateApiTestCaseStatusByApiId method in the ApiTestCase service")
    public void updateApiTestCaseStatusByApiId_test() {
        doNothing().when(customizedApiTestCaseRepository).updateApiTestCaseStatusByApiId(any(), any());
        apiTestCaseService.updateApiTestCaseStatusByApiId(Collections.singletonList(ObjectId.get().toString()),
            ApiBindingStatus.BINDING);
        verify(customizedApiTestCaseRepository, times(1)).updateApiTestCaseStatusByApiId(any(), any());
    }

    @Test
    @DisplayName("Test the deleteByIds method in the ApiTestCase service")
    public void deleteByIds_test() {
        List<String> ids = Collections.singletonList(ID);
        doNothing().when(apiTestCaseRepository).deleteAllByIdIn(ids);
        assertThat(apiTestCaseService.deleteByIds(ids)).isTrue();
    }

    @Test
    @DisplayName("Test the deleteAll method in the ApiTestCase service")
    public void deleteAll_test() {
        doNothing().when(apiTestCaseRepository).deleteAllByRemovedIsTrue();
        assertThat(apiTestCaseService.deleteAll()).isTrue();
    }
}