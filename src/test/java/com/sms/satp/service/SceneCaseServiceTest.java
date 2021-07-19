package com.sms.satp.service;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.dto.request.AddSceneCaseApi;
import com.sms.satp.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.ApiTestCaseRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.dto.response.SceneCaseApiConnResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.impl.SceneCaseServiceImpl;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for SceneCaseServiceTest")
class SceneCaseServiceTest {

    private SceneCaseRepository sceneCaseRepository;
    private CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private SceneCaseMapper sceneCaseMapper;
    private SceneCaseServiceImpl sceneCaseService;
    private SceneCaseApiService sceneCaseApiService;
    private CaseTemplateApiService caseTemplateApiService;
    private ApiTestCaseRepository apiTestCaseRepository;
    private ApiRepository apiRepository;
    private ApiTestCaseMapper apiTestCaseMapper;
    private SceneCaseApiRepository sceneCaseApiRepository;
    private CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private SceneCaseApiMapper sceneCaseApiMapper;
    private CaseTemplateApiMapper caseTemplateApiMapper;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static String MOCK_CREATE_USER_ID = ObjectId.get().toString();
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;

    @BeforeEach
    void setBean() {
        sceneCaseRepository = mock(SceneCaseRepository.class);
        customizedSceneCaseRepository = mock(CustomizedSceneCaseRepository.class);
        sceneCaseMapper = mock(SceneCaseMapper.class);
        sceneCaseApiService = mock(SceneCaseApiService.class);
        caseTemplateApiService = mock(CaseTemplateApiService.class);
        apiTestCaseRepository = mock(ApiTestCaseRepository.class);
        apiRepository = mock(ApiRepository.class);
        apiTestCaseMapper = mock(ApiTestCaseMapper.class);
        sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
        customizedSceneCaseApiRepository = mock(CustomizedSceneCaseApiRepository.class);
        sceneCaseApiMapper = mock(SceneCaseApiMapper.class);
        caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
        sceneCaseService = new SceneCaseServiceImpl(sceneCaseRepository, customizedSceneCaseRepository,
            sceneCaseMapper, sceneCaseApiService, caseTemplateApiService, apiTestCaseRepository,
            apiRepository, apiTestCaseMapper, sceneCaseApiRepository, customizedSceneCaseApiRepository,
            sceneCaseApiMapper, caseTemplateApiMapper);
    }

    @Test
    @DisplayName("Test the add method in the SceneCase service")
    void add_test() {
        SceneCase sceneCase =
            SceneCase.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCase.class))).thenReturn(sceneCase);
        Boolean isSuccess = sceneCaseService.add(AddSceneCaseRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the SceneCase service throws exception")
    void add_test_thenThrowException() {
        SceneCase sceneCase =
            SceneCase.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCase.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.add(AddSceneCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCase service")
    void deleteByIds_test() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doNothing().when(sceneCaseRepository).deleteById(any());
        List<SceneCaseApi> sceneCaseApiDtoList = Lists.newArrayList(SceneCaseApi.builder().build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiDtoList);
        when(sceneCaseApiService.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCase service throws exception")
    void deleteByIds_test_thenThrownException() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR)).when(sceneCaseRepository).deleteById(any());
        assertThatThrownBy(() -> sceneCaseService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service")
    void edit_test() {
        SceneCase sceneCase =
            SceneCase.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCase> optionalSceneCase = Optional.ofNullable(SceneCase.builder().removed(Boolean.TRUE).build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCase.class))).thenReturn(sceneCase);
        List<SceneCaseApi> sceneCaseApiDtoList = Lists
            .newArrayList(SceneCaseApi.builder().id(MOCK_ID).build());
        when(sceneCaseApiService.getApiBySceneCaseId(any(), anyBoolean())).thenReturn(sceneCaseApiDtoList);
        when(sceneCaseApiService.editAll(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.edit(UpdateSceneCaseRequest.builder().removed(Boolean.FALSE).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service throws exception")
    void edit_test_thenThrownException() {
        SceneCase sceneCase =
            SceneCase.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCase> optionalSceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCase.class)))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.edit(UpdateSceneCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the batch edit method in the SceneCase service")
    void batchEdit_test() {
        SceneCase sceneCase = SceneCase.builder().id(MOCK_ID).build();
        Optional<SceneCase> optionalSceneCase = Optional
            .ofNullable(SceneCase.builder().removed(Boolean.TRUE).build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCase.class))).thenReturn(sceneCase);
        List<SceneCaseApi> caseTemplateApiDtoList = Lists
            .newArrayList(SceneCaseApi.builder().id(MOCK_ID).build());
        when(sceneCaseApiService.getApiBySceneCaseId(any(), anyBoolean())).thenReturn(caseTemplateApiDtoList);
        when(caseTemplateApiService.editAll(any())).thenReturn(Boolean.TRUE);
        List<SceneCase> sceneCaseList = Lists.newArrayList(sceneCase);
        Boolean isSuccess = sceneCaseService.batchEdit(sceneCaseList);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the batch edit method in the SceneCase service thrown exception")
    void batchEdit_test_thrownException() {
        SceneCase sceneCase = SceneCase.builder().id(MOCK_ID).build();
        when(sceneCaseRepository.findById(any())).thenThrow(new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR));
        List<SceneCase> sceneCaseList = Lists.newArrayList(sceneCase);
        assertThatThrownBy(() -> sceneCaseService.batchEdit(sceneCaseList))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the search method in the SceneCase service")
    void search_test() {
        SearchSceneCaseRequest dto = new SearchSceneCaseRequest();
        dto.setName(MOCK_NAME);
        List<SceneCaseResponse> dtoList = Lists.newArrayList(SceneCaseResponse.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<SceneCaseResponse> sceneCaseDtoPage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(customizedSceneCaseRepository.search(any(), any())).thenReturn(sceneCaseDtoPage);
        Page<SceneCaseResponse> pageDto = sceneCaseService.page(dto, new ObjectId());
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the search method in the SceneCase service thrown exception")
    void search_test_thenThrownException() {
        SearchSceneCaseRequest dto = new SearchSceneCaseRequest();
        dto.setName(MOCK_NAME);
        when(customizedSceneCaseRepository.search(any(), any()))
            .thenThrow(new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.page(dto, new ObjectId()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getConn method in the SceneCase service")
    void getConn_test() {
        Optional<SceneCase> optional = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(optional);
        SceneCaseResponse sceneCaseDto = SceneCaseResponse.builder().build();
        when(sceneCaseMapper.toDto(any())).thenReturn(sceneCaseDto);
        List<SceneCaseApi> sceneCaseApiDtoList =
            Lists.newArrayList(
                SceneCaseApi.builder().caseTemplateId(MOCK_ID).id(MOCK_ID).caseTemplateApiConnList(Lists.newArrayList(
                    CaseTemplateApiConn.builder().caseTemplateApiId(MOCK_ID).execute(Boolean.TRUE).build())).build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiDtoList);
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(Lists.newArrayList());
        List<CaseTemplateApiResponse> caseTemplateApiResponses =
            Lists.newArrayList(CaseTemplateApiResponse.builder().build());
        when(caseTemplateApiMapper.toCaseTemplateApiDtoList(any())).thenReturn(caseTemplateApiResponses);
        SceneCaseApiConnResponse response =
            SceneCaseApiConnResponse.builder().id(MOCK_ID).caseTemplateId(MOCK_ID).build();
        when(sceneCaseApiMapper.toSceneCaseApiConnResponse(any())).thenReturn(response);
        SceneTemplateResponse dto = sceneCaseService.getConn(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getConn method in the SceneCase service thrown exception")
    void getConn_test_thrownException() {
        when(sceneCaseRepository.findById(any())).thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_CONN_ERROR));
        assertThatThrownBy(() -> sceneCaseService.getConn(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service")
    void editConn_test() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<SceneCaseApi> sceneCaseApi =
            Optional.ofNullable(SceneCaseApi.builder().apiTestCase(ApiTestCase.builder().build()).build());
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any())).thenReturn(Lists.newArrayList(SceneCaseApi.builder().build()));
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        Boolean isSuccess = sceneCaseService.editConn(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service")
    void editConnCaseTemplateIdNotNull_test() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<SceneCaseApi> sceneCaseApi =
            Optional.ofNullable(
                SceneCaseApi.builder().caseTemplateId(MOCK_ID).apiTestCase(ApiTestCase.builder().build()).build());
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any())).thenReturn(Lists.newArrayList(SceneCaseApi.builder().build()));
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        Boolean isSuccess = sceneCaseService.editConn(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service thrown exception")
    void editConn_test_thrownException() {
        Optional<SceneCase> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        assertThatThrownBy(() -> sceneCaseService.editConn(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the get method in the SceneCase service")
    void get_test() {
        List<SceneCase> sceneCaseList = Lists.newArrayList(SceneCase.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findAll(any(Example.class))).thenReturn(sceneCaseList);
        List<SceneCase> dto = sceneCaseService.get(MOCK_GROUP_ID, MOCK_PROJECT_ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("Test the get method in the SceneCase service thrown exception")
    void get_test_thrownException() {
        when(sceneCaseRepository.findAll(any(Example.class))).thenThrow(ApiTestPlatformException.class);
        assertThatThrownBy(() -> sceneCaseService.get(MOCK_GROUP_ID, MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addApi method in the SceneCase service")
    void addApi_test() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<ApiTestCase> apiTestCase = Optional.ofNullable(ApiTestCase.builder().build());
        when(apiTestCaseRepository.findById(any())).thenReturn(apiTestCase);
        when(sceneCaseApiRepository.insert(any(SceneCaseApi.class))).thenReturn(SceneCaseApi.builder().build());
        Optional<ApiEntity> apiEntity = Optional.ofNullable(ApiEntity.builder().build());
        when(apiRepository.findById(any())).thenReturn(apiEntity);
        ApiTestCase testCase = ApiTestCase.builder().id(MOCK_ID).build();
        when(apiTestCaseMapper.toEntityByApiEntity(any())).thenReturn(testCase);
        when(sceneCaseApiRepository.insert(any(SceneCaseApi.class))).thenReturn(SceneCaseApi.builder().build());
        AddSceneCaseApiByIdsRequest request = getAddRequest();
        Boolean isSuccess = sceneCaseService.addApi(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the addApi method in the SceneCase service thrown exception")
    void addApi_test_thrownException() {
        Optional<SceneCase> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        AddSceneCaseApiByIdsRequest request = getAddRequest();
        assertThatThrownBy(() -> sceneCaseService.addApi(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addTemplate method in the SceneCase service")
    void addTemplate_thenRight() {
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        when(customizedSceneCaseApiRepository.findCurrentOrderBySceneCaseId(any())).thenReturn(MOCK_PAGE);
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().build());
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(caseTemplateApiList);
        List<CaseTemplateApiConn> caseTemplateApiConnList = Lists
            .newArrayList(CaseTemplateApiConn.builder().execute(Boolean.TRUE).caseTemplateApiId(MOCK_ID).build());
        when(sceneCaseMapper.toCaseTemplateApiConnList(any())).thenReturn(caseTemplateApiConnList);
        when(sceneCaseApiRepository.insert(any(SceneCaseApi.class))).thenReturn(SceneCaseApi.builder().build());
        AddCaseTemplateConnRequest request = getAddConnRequest();
        Boolean isSuccess = sceneCaseService.addTemplate(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the addTemplate method in the SceneCase service thrown exception")
    void addTemplate_thenThrowException() {
        Optional<SceneCase> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        AddCaseTemplateConnRequest request = getAddConnRequest();
        assertThatThrownBy(() -> sceneCaseService.addTemplate(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteConn method in the SceneCase service")
    void deleteConn_thenRight() {
        doNothing().when(sceneCaseApiRepository).deleteById(any());
        Boolean isSuccess = sceneCaseService.deleteConn(MOCK_ID);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteConn method in the SceneCase service thrown exception")
    void deleteConn_thenThrownException() {
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_CONN_ERROR)).when(sceneCaseApiRepository)
            .deleteById(any());
        assertThatThrownBy(() -> sceneCaseService.deleteConn(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    private AddCaseTemplateConnRequest getAddConnRequest() {
        return AddCaseTemplateConnRequest.builder()
            .sceneCaseId(MOCK_ID)
            .caseTemplateIds(Lists.newArrayList(MOCK_ID)).build();
    }

    private AddSceneCaseApiByIdsRequest getAddRequest() {
        return AddSceneCaseApiByIdsRequest.builder()
            .sceneCaseId(MOCK_ID)
            .sceneCaseApis(Lists.newArrayList(
                AddSceneCaseApi.builder().id(MOCK_ID)
                    .isCase(Boolean.TRUE)
                    .order(MOCK_PAGE)
                    .build(),
                AddSceneCaseApi.builder().id(MOCK_ID)
                    .isCase(Boolean.FALSE)
                    .order(MOCK_PAGE)
                    .build()))
            .build();
    }

    private UpdateSceneCaseConnRequest getUpdateRequest() {
        return UpdateSceneCaseConnRequest.builder()
            .sceneCaseId(MOCK_ID)
            .updateSceneCaseApiConnRequest(
                Lists.newArrayList(
                    UpdateSceneCaseApiConnRequest.builder()
                        .apiTestCase(ApiTestCaseRequest.builder().execute(Boolean.TRUE).build())
                        .caseTemplateApiList(Lists.newArrayList(CaseTemplateApiResponse.builder().build()))
                        .build())
            ).build();
    }

}
