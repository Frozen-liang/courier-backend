package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddCaseTemplateApiRequest;
import com.sms.satp.dto.request.BatchAddCaseTemplateApiRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateApiRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.service.impl.CaseTemplateApiServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateApiServiceTest")
class CaseTemplateApiServiceTest {

    private final CaseTemplateApiRepository caseTemplateApiRepository= mock(CaseTemplateApiRepository.class);
    private final CaseTemplateApiMapper caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
    private final CaseTemplateRepository caseTemplateRepository = mock(CaseTemplateRepository.class);
    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private CaseTemplateApiServiceImpl caseTemplateApiService =
        new CaseTemplateApiServiceImpl(caseTemplateApiRepository, caseTemplateApiMapper,
        caseTemplateRepository, apiRepository, apiTestCaseMapper, apiTestCaseRepository);

    private final static String MOCK_SCENE_CASE_ID = "1";
    private final static String MOCK_ID = new ObjectId().toString();
    private final static Integer MOCK_ORDER_NUMBER = 1;

    @Test
    @DisplayName("Test the batchAdd method in the CaseTemplateApi service")
    void batchAdd_test() {
        BatchAddCaseTemplateApiRequest addCaseTemplateApiDto = getAddDto();
        CaseTemplateApi caseApi = CaseTemplateApi.builder().build();
        List<CaseTemplateApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class))).thenReturn(sceneCaseApiList);
        Boolean isSuccess = caseTemplateApiService.batchAdd(addCaseTemplateApiDto);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batchAdd method in the CaseTemplateApi service throws exception")
    void batchAdd_test_thenThrowException() {
        BatchAddCaseTemplateApiRequest addCaseTemplateApiDto = getAddDto();
        CaseTemplateApi caseApi = CaseTemplateApi.builder().build();
        List<CaseTemplateApi> sceneCaseApiList = Lists.newArrayList(caseApi);
        when(caseTemplateApiMapper.toCaseTemplateApiListByUpdateRequestList(any())).thenReturn(sceneCaseApiList);
        when(caseTemplateApiRepository.insert(any(List.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.batchAdd(addCaseTemplateApiDto))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateApi service")
    void deleteByIds_test() {
        Optional<CaseTemplateApi> caseTemplateApi = Optional.ofNullable(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        when(caseTemplateApiRepository.deleteAllByIdIsIn(any())).thenReturn(1L);
        Boolean isSuccess = caseTemplateApiService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplateApi service throws exception")
    void deleteByIds_test_thenThrowException() {
        Optional<CaseTemplateApi> caseTemplateApi = Optional.ofNullable(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findById(any())).thenReturn(caseTemplateApi);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR)).when(caseTemplateApiRepository)
            .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> caseTemplateApiService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service")
    void edit_test() {
        CaseTemplateApi sceneCaseApi = CaseTemplateApi.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenReturn(sceneCaseApi);
        Boolean isSuccess = caseTemplateApiService.edit(UpdateCaseTemplateApiRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplateApi service throws exception")
    void edit_thenThrowException() {
        CaseTemplateApi sceneCaseApi = CaseTemplateApi.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiByUpdateRequest(any())).thenReturn(sceneCaseApi);
        when(caseTemplateApiRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.edit(UpdateCaseTemplateApiRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editAll method in the CaseTemplateApi service")
    void editAll_test() {
        List<CaseTemplateApi> sceneCaseApi = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.saveAll(any())).thenReturn(sceneCaseApi);
        Boolean isSuccess = caseTemplateApiService.editAll(sceneCaseApi);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editAll method in the CaseTemplateApi service throws exception")
    void editAll_thenThrowException() {
        List<CaseTemplateApi> sceneCaseApi = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.saveAll(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.editAll(sceneCaseApi))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list method in the CaseTemplateApi service")
    void listBySceneCaseId_test() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class))).thenReturn(caseTemplateApiList);
        CaseTemplateApiResponse dto = CaseTemplateApiResponse.builder().build();
        when(caseTemplateApiMapper.toCaseTemplateApiDto(any())).thenReturn(dto);
        List<CaseTemplateApiResponse> dtoList = caseTemplateApiService
            .listByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list method in the CaseTemplateApi service throws exception")
    void listBySceneCaseId_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenReturnSceneCaseApi() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.findAll(any(Example.class))).thenReturn(caseTemplateApiList);
        List<CaseTemplateApi> dto = caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the list by caseTemplateId method in the CaseTemplateApi service")
    void listBySceneCaseId_test_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.listByCaseTemplateId(MOCK_SCENE_CASE_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getApiByCaseTemplateId method in the CaseTemplateApi service")
    void getApiByCaseTemplateId_test() {
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class))).thenReturn(caseTemplateApiList);
        List<CaseTemplateApi> dtoList = caseTemplateApiService
            .getApiByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE);
        assertThat(dtoList).isNotEmpty();
    }

    @Test
    @DisplayName("Test the getApiByCaseTemplateId method in the CaseTemplateApi service throws exception")
    void getApiByCaseTemplateId_thenThrowException() {
        when(caseTemplateApiRepository.findAll(any(), any(Sort.class)))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR));
        assertThatThrownBy(() -> caseTemplateApiService.getApiByCaseTemplateId(MOCK_SCENE_CASE_ID, Boolean.FALSE))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getCaseTemplateApiById method in the CaseTemplateApi service")
    void getCaseTemplateApiById_test() {
        CaseTemplateApi caseTemplateApi = CaseTemplateApi.builder().id(MOCK_ID).build();
        Optional<CaseTemplateApi> caseTemplateApiOptional = Optional.ofNullable(caseTemplateApi);
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

    private BatchAddCaseTemplateApiRequest getAddDto() {
        return BatchAddCaseTemplateApiRequest.builder()
            .addCaseTemplateApiRequestList(Lists.newArrayList(AddCaseTemplateApiRequest.builder().build()))
            .build();
    }

}
