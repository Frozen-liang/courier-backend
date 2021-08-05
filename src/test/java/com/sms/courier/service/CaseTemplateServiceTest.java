package com.sms.courier.service;

import com.google.common.collect.Lists;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AddCaseTemplateApiByIdsRequest;
import com.sms.courier.dto.request.AddCaseTemplateRequest;
import com.sms.courier.dto.request.AddSceneCaseApi;
import com.sms.courier.dto.request.CaseTemplateSearchRequest;
import com.sms.courier.dto.request.ConvertCaseTemplateRequest;
import com.sms.courier.dto.request.UpdateCaseTemplateRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.CaseTemplateDetailResponse;
import com.sms.courier.dto.response.CaseTemplateResponse;
import com.sms.courier.dto.response.IdResponse;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.CaseTemplateMapper;
import com.sms.courier.repository.ApiRepository;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CaseTemplateRepository;
import com.sms.courier.repository.CustomizedCaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedCaseTemplateRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.service.impl.CaseTemplateServiceImpl;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.courier.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RECOVER_CASE_TEMPLATE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for CaseTemplateServiceTest")
class CaseTemplateServiceTest {

    private final CaseTemplateRepository caseTemplateRepository = mock(CaseTemplateRepository.class);
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository = mock(
        CustomizedCaseTemplateRepository.class);
    private final CaseTemplateMapper caseTemplateMapper = mock(CaseTemplateMapper.class);
    private final CaseTemplateApiService caseTemplateApiService = mock(CaseTemplateApiService.class);
    private final SceneCaseRepository sceneCaseRepository = mock(SceneCaseRepository.class);
    private final SceneCaseApiService sceneCaseApiService = mock(SceneCaseApiService.class);
    private final CaseTemplateApiMapper caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final ApiRepository apiRepository = mock(ApiRepository.class);
    private final ApiTestCaseMapper apiTestCaseMapper = mock(ApiTestCaseMapper.class);
    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository =
        mock(CustomizedCaseTemplateApiRepository.class);
    private final CaseTemplateServiceImpl caseTemplateService = new CaseTemplateServiceImpl(caseTemplateRepository,
        customizedCaseTemplateRepository, caseTemplateMapper, caseTemplateApiService,
        sceneCaseRepository, sceneCaseApiService, caseTemplateApiMapper,
        caseTemplateApiRepository, apiRepository, apiTestCaseMapper, apiTestCaseRepository,
        customizedCaseTemplateApiRepository);

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static String MOCK_CREATE_USER_ID = ObjectId.get().toString();
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;

    @Test
    @DisplayName("Test the add method in the CaseTemplate service")
    void add_test() {
        CaseTemplateEntity caseTemplate =
            CaseTemplateEntity.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(caseTemplateMapper.toCaseTemplateByAddRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplateEntity.class))).thenReturn(caseTemplate);
        Boolean isSuccess = caseTemplateService.add(AddCaseTemplateRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplate service throws exception")
    void add_test_thenThrowException() {
        CaseTemplateEntity caseTemplate =
            CaseTemplateEntity.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(caseTemplateMapper.toCaseTemplateByAddRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplateEntity.class)))
            .thenThrow(new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.add(AddCaseTemplateRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the add method in the CaseTemplate service throws exception")
    void add_test_thenThrow_Exception() {
        CaseTemplateEntity caseTemplate =
            CaseTemplateEntity.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(caseTemplateMapper.toCaseTemplateByAddRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplateEntity.class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateService.add(AddCaseTemplateRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addConvert method in the CaseTemplate service")
    void addConvert_test_thenRight() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        CaseTemplateEntity caseTemplate = CaseTemplateEntity.builder().build();
        when(caseTemplateMapper.toCaseTemplateBySceneCase(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.insert(any(CaseTemplateEntity.class))).thenReturn(caseTemplate);
        List<SceneCaseApiEntity> sceneCaseApiList =
            Lists.newArrayList(SceneCaseApiEntity.builder().id(MOCK_ID).order(MOCK_SIZE).build(),
                SceneCaseApiEntity.builder().id(MOCK_ID).order(MOCK_SIZE).caseTemplateId(MOCK_ID).build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiList);
        CaseTemplateApiEntity caseTemplateApi = CaseTemplateApiEntity.builder().id(MOCK_ID).order(MOCK_PAGE).build();
        when(caseTemplateApiMapper.toCaseTemplateApiBySceneCaseApi(any())).thenReturn(caseTemplateApi);
        List<CaseTemplateApiEntity> templateApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiRepository.findAllByCaseTemplateIdOrderByOrder(any())).thenReturn(templateApiList);
        when(caseTemplateApiRepository.insert(any(List.class))).thenReturn(templateApiList);
        IdResponse response =
            caseTemplateService.add(ConvertCaseTemplateRequest.builder().sceneCaseId(MOCK_ID).groupId(MOCK_ID).build());
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the addConvert method in the CaseTemplate service throw exception")
    void addConvertCaseIsNull_test_thenRight() {
        Optional<SceneCaseEntity> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        assertThatThrownBy(() -> caseTemplateService
            .add(ConvertCaseTemplateRequest.builder().sceneCaseId(MOCK_ID).groupId(MOCK_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addConvert method in the CaseTemplate service throw exception")
    void addConvertCase_test_throwException() {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateService
            .add(ConvertCaseTemplateRequest.builder().sceneCaseId(MOCK_ID).groupId(MOCK_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the CaseTemplate service")
    void deleteByIds_test() {
        doNothing().when(caseTemplateRepository).deleteById(any());
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(caseTemplateApiList);
        when(caseTemplateApiService.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = caseTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplate service throws exception")
    void deleteByIds_test_thenThrownException() {
        doThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR)).when(caseTemplateRepository)
            .deleteById(any());
        assertThatThrownBy(() -> caseTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteById method in the CaseTemplate service throws exception")
    void deleteByIds_test_then_thrownException() {
        doThrow(new RuntimeException()).when(caseTemplateRepository)
            .deleteById(any());
        assertThatThrownBy(() -> caseTemplateService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplate service")
    void edit_test() {
        CaseTemplateEntity caseTemplate =
            CaseTemplateEntity.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(caseTemplateMapper.toCaseTemplateByUpdateRequest(any())).thenReturn(caseTemplate);
        Optional<CaseTemplateEntity> optionalSceneCase = Optional
            .ofNullable(CaseTemplateEntity.builder().removed(Boolean.TRUE).build());
        when(caseTemplateRepository.findById(any())).thenReturn(optionalSceneCase);
        when(caseTemplateRepository.save(any(CaseTemplateEntity.class))).thenReturn(caseTemplate);
        List<CaseTemplateApiEntity> caseTemplateApiDtoList = Lists
            .newArrayList(CaseTemplateApiEntity.builder().id(MOCK_ID).build());
        when(caseTemplateApiService.getApiByCaseTemplateId(any(), anyBoolean())).thenReturn(caseTemplateApiDtoList);
        Boolean isSuccess = caseTemplateService
            .edit(UpdateCaseTemplateRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the CaseTemplate service throws exception")
    void edit_test_thenThrownException() {
        CaseTemplateEntity caseTemplate =
            CaseTemplateEntity.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(caseTemplateMapper.toCaseTemplateByUpdateRequest(any())).thenReturn(caseTemplate);
        when(caseTemplateRepository.save(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.edit(UpdateCaseTemplateRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the search method in the CaseTemplate service")
    void page_test() {
        CaseTemplateSearchRequest dto = new CaseTemplateSearchRequest();
        dto.setName(MOCK_NAME);
        List<CaseTemplateResponse> dtoList = Lists.newArrayList(CaseTemplateResponse.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<CaseTemplateResponse> caseTemplatePage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(customizedCaseTemplateRepository.page(any(), any())).thenReturn(caseTemplatePage);
        Page<CaseTemplateResponse> pageDto = caseTemplateService.page(dto, new ObjectId());
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the search method in the CaseTemplate service thrown exception")
    void page_test_thenThrownException() {
        CaseTemplateSearchRequest dto = new CaseTemplateSearchRequest();
        dto.setName(MOCK_NAME);
        when(customizedCaseTemplateRepository.page(any(), any()))
            .thenThrow(new ApiTestPlatformException(SEARCH_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.page(dto, new ObjectId()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the get method in the CaseTemplate service")
    void get_thenRight() {
        List<CaseTemplateEntity> caseTemplateList = Lists.newArrayList(CaseTemplateEntity.builder().build());
        when(caseTemplateRepository.findAll(any(Example.class))).thenReturn(caseTemplateList);
        List<CaseTemplateEntity> dto = caseTemplateService.get(MOCK_ID, MOCK_ID);
        assertThat(dto.size()).isEqualTo(MOCK_SIZE);
    }

    @Test
    @DisplayName("Test the get method in the CaseTemplate service thrown exception")
    void get_thenThrownException() {
        when(caseTemplateRepository.findAll(any(Example.class)))
            .thenThrow(new ApiTestPlatformException(GET_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.get(MOCK_ID, MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getApiList method in the CaseTemplate service")
    void getApiList_thenRight() {
        Optional<CaseTemplateEntity> caseTemplate = Optional.ofNullable(CaseTemplateEntity.builder().build());
        when(caseTemplateRepository.findById(any())).thenReturn(caseTemplate);
        List<CaseTemplateApiResponse> caseTemplateApiResponseList =
            Lists.newArrayList(CaseTemplateApiResponse.builder().build());
        when(caseTemplateApiService.listResponseByCaseTemplateId(any())).thenReturn(caseTemplateApiResponseList);
        CaseTemplateDetailResponse response = caseTemplateService.getApiList(MOCK_ID);
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("Test the getApiList method in the CaseTemplate service thrown exception")
    void getApiListCaseTemplateIsNull_thenThrownException() {
        Optional<CaseTemplateEntity> caseTemplate = Optional.empty();
        when(caseTemplateRepository.findById(any())).thenReturn(caseTemplate);
        assertThatThrownBy(() -> caseTemplateService.getApiList(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getApiList method in the CaseTemplate service thrown exception")
    void getApiListCaseTemplateIsNull_then_thrownException() {
        when(caseTemplateRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateService.getApiList(MOCK_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addApi method in the CaseTemplate service")
    void addApi_thenRight() {
        Optional<CaseTemplateEntity> caseTemplate = Optional.ofNullable(CaseTemplateEntity.builder().build());
        when(caseTemplateRepository.findById(any())).thenReturn(caseTemplate);
        Optional<ApiTestCaseEntity> apiTestCase = Optional.ofNullable(ApiTestCaseEntity.builder().id(MOCK_ID).build());
        when(apiTestCaseRepository.findById(any())).thenReturn(apiTestCase);
        when(caseTemplateApiRepository.insert(any(CaseTemplateApiEntity.class)))
            .thenReturn(CaseTemplateApiEntity.builder().build());
        Optional<ApiEntity> apiEntity = Optional.ofNullable(ApiEntity.builder().id(MOCK_ID).build());
        when(apiRepository.findById(any())).thenReturn(apiEntity);
        ApiTestCaseEntity testCase = ApiTestCaseEntity.builder().id(MOCK_ID).build();
        when(apiTestCaseMapper.toEntityByApiEntity(any())).thenReturn(testCase);
        when(caseTemplateApiRepository.insert(any(CaseTemplateApiEntity.class)))
            .thenReturn(CaseTemplateApiEntity.builder().build());
        AddCaseTemplateApiByIdsRequest request = getAddRequest();
        Boolean isSuccess = caseTemplateService.addApi(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the addApi method in the CaseTemplate service thrown exception")
    void addApiCaseTemplateIsNull_thenThrownException() {
        Optional<CaseTemplateEntity> caseTemplate = Optional.empty();
        when(caseTemplateRepository.findById(any())).thenReturn(caseTemplate);
        AddCaseTemplateApiByIdsRequest request = getAddRequest();
        assertThatThrownBy(() -> caseTemplateService.addApi(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addApi method in the CaseTemplate service thrown exception")
    void addApi_thenThrownException() {
        when(caseTemplateRepository.findById(any())).thenThrow(new RuntimeException());
        AddCaseTemplateApiByIdsRequest request = getAddRequest();
        assertThatThrownBy(() -> caseTemplateService.addApi(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the CaseTemplate service")
    void delete_test() {
        when(customizedCaseTemplateRepository.deleteByIds(any())).thenReturn(Boolean.TRUE);
        List<CaseTemplateApiEntity> caseTemplateApiEntityList = Lists.newArrayList(getCaseTemplateApiEntity());
        when(customizedCaseTemplateApiRepository.findCaseTemplateApiIdsByCaseTemplateIds(any()))
            .thenReturn(caseTemplateApiEntityList);
        when(customizedCaseTemplateApiRepository.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = caseTemplateService.delete(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the delete method in the CaseTemplate service thrown exception")
    void delete_test_throwApiException() {
        when(customizedCaseTemplateRepository.deleteByIds(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.delete(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the CaseTemplate service thrown exception")
    void delete_test_throwException() {
        when(customizedCaseTemplateRepository.deleteByIds(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateService.delete(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the recover method in the CaseTemplate service")
    void recover_test() {
        when(customizedCaseTemplateRepository.recover(any())).thenReturn(Boolean.TRUE);
        List<CaseTemplateApiEntity> caseTemplateApiEntityList = Lists.newArrayList(getCaseTemplateApiEntity());
        when(customizedCaseTemplateApiRepository.findCaseTemplateApiIdsByCaseTemplateIds(any()))
            .thenReturn(caseTemplateApiEntityList);
        when(customizedCaseTemplateApiRepository.recover(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = caseTemplateService.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the recover method in the CaseTemplate service thrown exception")
    void recover_test_thrownApiException() {
        when(customizedCaseTemplateRepository.recover(any()))
            .thenThrow(new ApiTestPlatformException(RECOVER_CASE_TEMPLATE_ERROR));
        assertThatThrownBy(() -> caseTemplateService.recover(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the recover method in the CaseTemplate service thrown exception")
    void recover_test_thrownException() {
        when(customizedCaseTemplateRepository.recover(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> caseTemplateService.recover(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private CaseTemplateApiEntity getCaseTemplateApiEntity() {
        return CaseTemplateApiEntity.builder()
            .build();
    }

    private AddCaseTemplateApiByIdsRequest getAddRequest() {
        return AddCaseTemplateApiByIdsRequest.builder()
            .caseTemplateId(MOCK_ID)
            .caseTemplateApis(Lists.newArrayList(AddSceneCaseApi.builder()
                    .id(MOCK_ID)
                    .isCase(Boolean.TRUE)
                    .order(MOCK_PAGE)
                    .build(),
                AddSceneCaseApi.builder()
                    .id(MOCK_ID)
                    .isCase(Boolean.FALSE)
                    .order(MOCK_SIZE)
                    .build())).build();
    }

}
