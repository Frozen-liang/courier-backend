package com.sms.courier.service;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.AddCaseTemplateApiRequest;
import com.sms.courier.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.courier.dto.request.BatchUpdateCaseTemplateApiRequest;
import com.sms.courier.dto.request.SyncApiRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.service.impl.CaseTemplateApiServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.BATCH_EDIT_CASE_TEMPLATE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateApiServiceTest")
class CaseTemplateApiServiceTest {

    private final CaseApiCountHandler caseApiCountHandler = mock(CaseApiCountHandler.class);
    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final CaseTemplateApiMapper caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository =
        mock(CustomizedSceneCaseApiRepository.class);
    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ParamInfoMapper paramInfoMapper = mock(ParamInfoMapper.class);
    private CaseTemplateApiServiceImpl caseTemplateApiService = new CaseTemplateApiServiceImpl(
        caseTemplateApiRepository, caseTemplateApiMapper, customizedSceneCaseApiRepository,
        caseApiCountHandler, apiRepository, paramInfoMapper);

    private final static String MOCK_SCENE_CASE_ID = "1";
    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_ORDER_NUMBER = 1;

    @Test
    @DisplayName("Test the batchAdd method in the CaseTemplateApi service")
    void batchAdd_test() {
        BatchAddCaseTemplateApiRequest addCaseTemplateApiDto = getAddDto();
        CaseTemplateApiEntity caseApi = CaseTemplateApiEntity.builder().build();
        List<CaseTemplateApiEntity> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class))).thenReturn(sceneCaseApiList);
        Boolean isSuccess = caseTemplateApiService.batchAdd(addCaseTemplateApiDto);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batchAdd method in the CaseTemplateApi service throws exception")
    void batchAdd_test_thenThrowException() {
        BatchAddCaseTemplateApiRequest addCaseTemplateApiDto = getAddDto();
        CaseTemplateApiEntity caseApi = CaseTemplateApiEntity.builder().build();
        List<CaseTemplateApiEntity> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.batchAdd(addCaseTemplateApiDto))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateApi service")
    void deleteByIds_test() {
        Optional<CaseTemplateApiEntity> caseTemplateApi = Optional.ofNullable(
            CaseTemplateApiEntity.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        when(caseTemplateApiRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        Boolean isSuccess = caseTemplateApiService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateApi service throws exception")
    void deleteByIds_test_thenThrowException() {
        Optional<CaseTemplateApiEntity> caseTemplateApi = Optional.ofNullable(
            CaseTemplateApiEntity.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR)).when(caseTemplateApiRepository)
            .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> caseTemplateApiService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service")
    void edit_test() {
        CaseTemplateApiEntity sceneCaseApi = CaseTemplateApiEntity.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenReturn(sceneCaseApi);
        Boolean isSuccess = caseTemplateApiService.edit(UpdateCaseTemplateApiRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service throws exception")
    void edit_thenThrowException() {
        CaseTemplateApiEntity sceneCaseApi = CaseTemplateApiEntity.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.edit(UpdateCaseTemplateApiRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batchEdit method in the CaseTemplateApi service")
    void batchEdit_test() {
        BatchUpdateCaseTemplateApiRequest dto = getUpdateSortOrder();
        CaseTemplateApiEntity caseTemplateApi = CaseTemplateApiEntity.builder().id(MOCK_ID).build();
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(caseTemplateApi);
        when(caseTemplateApiRepository.saveAll(any())).thenReturn(caseTemplateApiList);
        Boolean isSuccess = caseTemplateApiService.batchEdit(dto);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batchEdit method in the CaseTemplateApi service throws exception")
    void batchEdit_thenThrowException() {
        BatchUpdateCaseTemplateApiRequest dto = getUpdateSortOrder();
        when(caseTemplateApiRepository.saveAll(any()))
            .thenThrow(new ApiTestPlatformException(BATCH_EDIT_CASE_TEMPLATE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.batchEdit(dto)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the listResponseByCaseTemplateId method in the CaseTemplateApi service")
    void listResponseByCaseTemplateId_test() {
        List<CaseTemplateApiEntity> sceneCaseApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(sceneCaseApiList);
        CaseTemplateApiResponse caseTemplateApiResponse = CaseTemplateApiResponse.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiDto(any())).thenReturn(caseTemplateApiResponse);
        List<CaseTemplateApiResponse> response = caseTemplateApiService.listResponseByCaseTemplateId(MOCK_ID);
        assertThat(response.size()).isEqualTo(MOCK_ORDER_NUMBER);
    }

    @Test
    @DisplayName("Test the listResponseByCaseTemplateId method in the CaseTemplateApi service thrown exception")
    void listResponseByCaseTemplateId_test_thrownException() {
        List<CaseTemplateApiEntity> sceneCaseApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiRepository.findAll(any(Example.class), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_API_LIST_BY_CASE_TEMPLATE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listResponseByCaseTemplateId(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenReturnSceneCaseApi() {
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(caseTemplateApiList);
        List<CaseTemplateApiEntity> dto = caseTemplateApiService
            .listByCaseTemplateId(MOCK_SCENE_CASE_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(Example.class), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getCaseTemplateApiById method in the CaseTemplateApi service")
    void getCaseTemplateApiById_test() {
        CaseTemplateApiEntity caseTemplateApi = CaseTemplateApiEntity.builder().id(MOCK_ID).build();
        Optional<CaseTemplateApiEntity> caseTemplateApiOptional = Optional.ofNullable(caseTemplateApi);
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApiOptional);
        CaseTemplateApiResponse caseTemplateApiDto = CaseTemplateApiResponse.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiDto(any())).thenReturn(caseTemplateApiDto);
        CaseTemplateApiResponse dto = caseTemplateApiService.getCaseTemplateApiById(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getCaseTemplateApiById method in the CaseTemplateApi service throws exception")
    void getCaseTemplateApiById_testThrowException() {
        when(caseTemplateApiRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.getCaseTemplateApiById(MOCK_ID));
    }

    @Test
    @DisplayName("Test the deleteAllByCaseTemplateIds method in the CaseTemplateApi service")
    void deleteAllByCaseTemplateIds_test() {
        when(caseTemplateApiRepository.deleteAllByCaseTemplateIdIsIn(any())).thenReturn(1L);
        caseTemplateApiService.deleteAllByCaseTemplateIds(List.of(MOCK_ID));
        verify(caseTemplateApiRepository, times(1)).deleteAllByCaseTemplateIdIsIn(any());
    }

    @Test
    @DisplayName("Test the syncApi method in the CaseTemplateApi service")
    public void syncApi_test() {
        Optional<CaseTemplateApiEntity> caseTemplateApiEntity =
            Optional.of(CaseTemplateApiEntity.builder().apiTestCase(ApiTestCaseEntity.builder().apiEntity(
                ApiEntity.builder().build()).build()).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApiEntity);
        when(caseTemplateApiRepository.save(any())).thenReturn(caseTemplateApiEntity.get());
        Optional<ApiEntity> apiEntity = Optional.of(ApiEntity.builder().build());
        when(apiRepository.findById(any())).thenReturn(apiEntity);
        Boolean isSuccess = caseTemplateApiService.syncApi(SyncApiRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while sync api")
    public void syncApi_smsException_test() {
        when(caseTemplateApiRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.CASE_SYNC_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.syncApi(SyncApiRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sync api")
    public void syncApi_exception_test() {
        when(caseTemplateApiRepository.findById(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateApiService.syncApi(SyncApiRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private BatchAddCaseTemplateApiRequest getAddDto() {
        return BatchAddCaseTemplateApiRequest.builder()
            .addCaseTemplateApiRequestList(Lists.newArrayList(AddCaseTemplateApiRequest.builder().build()))
            .build();
    }

    private BatchUpdateCaseTemplateApiRequest getUpdateSortOrder() {
        return BatchUpdateCaseTemplateApiRequest.builder()
            .updateCaseTemplateApiRequestList(Lists.newArrayList(UpdateCaseTemplateApiRequest.builder()
                .caseTemplateId(MOCK_SCENE_CASE_ID)
                .order(MOCK_ORDER_NUMBER)
                .build())).build();
    }

}
