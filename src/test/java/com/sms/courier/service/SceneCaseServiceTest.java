package com.sms.courier.service;

import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.ENTITY_SERIALIZATION_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RECOVER_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.AddCaseTemplateApi;
import com.sms.courier.dto.request.AddCaseTemplateConnRequest;
import com.sms.courier.dto.request.AddSceneCaseApi;
import com.sms.courier.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.ApiTestCaseRequest;
import com.sms.courier.dto.request.CopyStepsRequest;
import com.sms.courier.dto.request.CopyStepsRequest.CaseOrderRequest;
import com.sms.courier.dto.request.EnvDataCollConnRequest;
import com.sms.courier.dto.request.ReviewRequest;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.CaseTemplateApiResponse;
import com.sms.courier.dto.response.EnvDataCollResponse;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseConnResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.response.SceneTemplateResponse;
import com.sms.courier.dto.vo.SceneCaseApiVo;
import com.sms.courier.entity.api.ApiEntity;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.DataCollectionMapper;
import com.sms.courier.mapper.MatchParamInfoMapper;
import com.sms.courier.mapper.ProjectEnvironmentMapper;
import com.sms.courier.mapper.SceneCaseApiMapper;
import com.sms.courier.mapper.SceneCaseMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CustomizedCaseTemplateApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseCommentRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.service.impl.SceneCaseServiceImpl;
import com.sms.courier.utils.SecurityUtil;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DisplayName("Test cases for SceneCaseServiceTest")
class SceneCaseServiceTest {

    private SceneCaseRepository sceneCaseRepository;
    private CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private SceneCaseMapper sceneCaseMapper;
    private SceneCaseServiceImpl sceneCaseService;
    private SceneCaseApiService sceneCaseApiService;
    private CaseTemplateApiService caseTemplateApiService;
    private ApiTestCaseRepository apiTestCaseRepository;
    private ApiTestCaseMapper apiTestCaseMapper;
    private SceneCaseApiRepository sceneCaseApiRepository;
    private CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private SceneCaseApiMapper sceneCaseApiMapper;
    private CaseTemplateApiMapper caseTemplateApiMapper;
    private CaseApiCountHandler caseApiCountHandler;
    private MatchParamInfoMapper matchParamInfoMapper;
    private ScheduleService scheduleService;
    private ProjectEnvironmentService projectEnvironmentService;
    private ProjectEnvironmentMapper projectEnvironmentMapper;
    private DataCollectionService dataCollectionService;
    private DataCollectionMapper dataCollectionMapper;
    private ObjectMapper objectMapper = mock(ObjectMapper.class);
    private CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;
    private SceneCaseCommentRepository sceneCaseCommentRepository;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static String MOCK_CREATE_USER_ID = ObjectId.get().toString();
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;
    private final static MockedStatic<SecurityUtil> SECURITY_UTIL_MOCKED_STATIC;

    @BeforeEach
    void setBean() {
        sceneCaseRepository = mock(SceneCaseRepository.class);
        customizedSceneCaseRepository = mock(CustomizedSceneCaseRepository.class);
        sceneCaseMapper = mock(SceneCaseMapper.class);
        sceneCaseApiService = mock(SceneCaseApiService.class);
        caseTemplateApiService = mock(CaseTemplateApiService.class);
        apiTestCaseRepository = mock(ApiTestCaseRepository.class);
        apiTestCaseMapper = mock(ApiTestCaseMapper.class);
        sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
        customizedSceneCaseApiRepository = mock(CustomizedSceneCaseApiRepository.class);
        sceneCaseApiMapper = mock(SceneCaseApiMapper.class);
        caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
        caseApiCountHandler = mock(CaseApiCountHandler.class);
        matchParamInfoMapper = mock(MatchParamInfoMapper.class);
        scheduleService = mock(ScheduleService.class);
        projectEnvironmentService = mock(ProjectEnvironmentService.class);
        projectEnvironmentMapper = mock(ProjectEnvironmentMapper.class);
        dataCollectionService = mock(DataCollectionService.class);
        dataCollectionMapper = mock(DataCollectionMapper.class);
        customizedCaseTemplateApiRepository = mock(CustomizedCaseTemplateApiRepository.class);
        sceneCaseCommentRepository = mock(SceneCaseCommentRepository.class);
        sceneCaseService = new SceneCaseServiceImpl(sceneCaseRepository,
            customizedSceneCaseRepository,
            sceneCaseMapper, sceneCaseApiService,
            caseTemplateApiService,
            apiTestCaseRepository,
            apiTestCaseMapper, sceneCaseApiRepository,
            customizedSceneCaseApiRepository,
            sceneCaseApiMapper, caseTemplateApiMapper,
            caseApiCountHandler, matchParamInfoMapper,
            scheduleService, projectEnvironmentService, projectEnvironmentMapper, dataCollectionService,
            dataCollectionMapper, objectMapper, customizedCaseTemplateApiRepository, sceneCaseCommentRepository);
    }

    static {
        SECURITY_UTIL_MOCKED_STATIC = mockStatic(SecurityUtil.class);
        SECURITY_UTIL_MOCKED_STATIC.when(SecurityUtil::getCurrUserId).thenReturn(ObjectId.get().toString());
    }

    @AfterAll
    public static void close() {
        SECURITY_UTIL_MOCKED_STATIC.close();
    }


    @Test
    @DisplayName("Test the add method in the SceneCase service")
    void add_test() {
        SceneCaseEntity sceneCase =
            SceneCaseEntity.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCaseEntity.class))).thenReturn(sceneCase);
        Boolean isSuccess = sceneCaseService.add(AddSceneCaseRequest.builder().build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the SceneCase service throws exception")
    void add_test_thenThrowException() {
        SceneCaseEntity sceneCase =
            SceneCaseEntity.builder().name(MOCK_NAME).projectId(MOCK_PROJECT_ID).groupId(MOCK_GROUP_ID)
                .createUserId(MOCK_CREATE_USER_ID).build();
        when(sceneCaseMapper.toAddSceneCase(any())).thenReturn(sceneCase);
        when(sceneCaseRepository.insert(any(SceneCaseEntity.class)))
            .thenThrow(new ApiTestPlatformException(ADD_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.add(AddSceneCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCase service")
    void deleteByIds_test() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doNothing().when(sceneCaseRepository).deleteById(any());
        List<SceneCaseApiEntity> sceneCaseApiDtoList = Lists.newArrayList(SceneCaseApiEntity.builder().build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiDtoList);
        when(sceneCaseApiService.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.deleteByIds(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCase service throws exception")
    void deleteByIds_test_thenThrownException() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR)).when(sceneCaseRepository)
            .deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> sceneCaseService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the deleteByIds method in the SceneCase service throws exception")
    void deleteByIds_test_thenThrown_Exception() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        doThrow(new RuntimeException()).when(sceneCaseRepository).deleteAllByIdIsIn(any());
        assertThatThrownBy(() -> sceneCaseService.deleteByIds(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service")
    void edit_test() {
        SceneCaseEntity sceneCase =
            SceneCaseEntity.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCaseEntity> optionalSceneCase = Optional.ofNullable(
            SceneCaseEntity.builder().removed(Boolean.TRUE).build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCaseEntity.class))).thenReturn(sceneCase);
        Boolean isSuccess =
            sceneCaseService.edit(UpdateSceneCaseRequest.builder().envDataCollConnList(
                Lists.newArrayList(EnvDataCollConnRequest.builder().envId(MOCK_ID).dataCollId(MOCK_GROUP_ID).build()))
                .build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCase service throws exception")
    void edit_exception_test() {
        SceneCaseEntity sceneCase =
            SceneCaseEntity.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCaseEntity> optionalSceneCase = Optional.ofNullable(
            SceneCaseEntity.builder().removed(Boolean.TRUE).build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCaseEntity.class))).thenReturn(sceneCase);
        EnvDataCollConnRequest request =
            EnvDataCollConnRequest.builder().envId(MOCK_ID).dataCollId(MOCK_GROUP_ID).build();
        assertThatThrownBy(() -> sceneCaseService.edit(UpdateSceneCaseRequest.builder()
            .envDataCollConnList(Lists.newArrayList(request, request))
            .build())).isInstanceOf(ApiTestPlatformException.class);


    }


    @Test
    @DisplayName("Test the edit method in the SceneCase service throws exception")
    void edit_test_thenThrownException() {
        SceneCaseEntity sceneCase =
            SceneCaseEntity.builder().id(MOCK_ID).modifyUserId(MOCK_CREATE_USER_ID).name(MOCK_NAME)
                .removed(Boolean.FALSE).build();
        when(sceneCaseMapper.toUpdateSceneCase(any())).thenReturn(sceneCase);
        Optional<SceneCaseEntity> optionalSceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(optionalSceneCase);
        when(sceneCaseRepository.save(any(SceneCaseEntity.class)))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.edit(UpdateSceneCaseRequest.builder().build()))
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
        Optional<SceneCaseResponse> optional = Optional
            .ofNullable(SceneCaseResponse.builder().envDataCollConnList(Lists.newArrayList(
                EnvDataCollResponse.builder().envId(MOCK_ID).dataCollId(MOCK_ID).build())).build());
        when(customizedSceneCaseRepository.findById(any())).thenReturn(optional);
        SceneCaseResponse sceneCaseDto = SceneCaseResponse.builder().build();
        when(sceneCaseMapper.toDto(any())).thenReturn(sceneCaseDto);
        List<SceneCaseApiEntity> sceneCaseApiDtoList =
            Lists.newArrayList(
                SceneCaseApiEntity.builder().caseTemplateId(MOCK_ID).id(MOCK_ID)
                    .caseTemplateApiConnList(Lists.newArrayList(
                        CaseTemplateApiConn.builder().caseTemplateApiId(MOCK_ID).execute(Boolean.TRUE).build()))
                    .build());
        when(sceneCaseApiService.listBySceneCaseId(any())).thenReturn(sceneCaseApiDtoList);
        List<CaseTemplateApiEntity> caseTemplateApiList =
            Lists.newArrayList(CaseTemplateApiEntity.builder().id(MOCK_ID)
                .apiTestCase(ApiTestCaseEntity.builder().id(MOCK_ID).execute(Boolean.TRUE).build()).build());
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(caseTemplateApiList);
        List<CaseTemplateApiResponse> caseTemplateApiResponses =
            Lists.newArrayList(CaseTemplateApiResponse.builder().build());
        when(caseTemplateApiMapper.toCaseTemplateApiDtoList(any())).thenReturn(caseTemplateApiResponses);
        SceneCaseConnResponse connResponse = SceneCaseConnResponse.builder().id(MOCK_ID).build();
        when(sceneCaseMapper.toConnResponse(any())).thenReturn(connResponse);
        SceneCaseApiConnResponse response =
            SceneCaseApiConnResponse.builder().id(MOCK_ID).caseTemplateId(MOCK_ID).build();
        when(sceneCaseApiMapper.toSceneCaseApiConnResponse(any())).thenReturn(response);
        SceneTemplateResponse dto = sceneCaseService.getConn(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the getConn method in the SceneCase service thrown exception")
    void getConn_test_thrownException() {
        when(customizedSceneCaseRepository.findById(any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_CONN_ERROR));
        assertThatThrownBy(() -> sceneCaseService.getConn(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getConn method in the SceneCase service thrown exception")
    void getConn_test_thrown_Exception() {
        when(customizedSceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sceneCaseService.getConn(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service")
    void editConn_test() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<SceneCaseApiEntity> sceneCaseApi =
            Optional.ofNullable(SceneCaseApiEntity.builder().apiTestCase(ApiTestCaseEntity.builder().build()).build());
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any()))
            .thenReturn(Lists.newArrayList(SceneCaseApiEntity.builder().build()));
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        Boolean isSuccess = sceneCaseService.editConn(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service")
    void editConnCaseTemplateIdNotNull_test() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<SceneCaseApiEntity> sceneCaseApi =
            Optional.ofNullable(
                SceneCaseApiEntity.builder().caseTemplateId(MOCK_ID).apiTestCase(ApiTestCaseEntity.builder().build())
                    .build());
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApi);
        when(sceneCaseApiRepository.saveAll(any()))
            .thenReturn(Lists.newArrayList(SceneCaseApiEntity.builder().build()));
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        Boolean isSuccess = sceneCaseService.editConn(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service thrown exception")
    void editConn_test_thrownException() {
        Optional<SceneCaseEntity> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        assertThatThrownBy(() -> sceneCaseService.editConn(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service thrown exception")
    void editConn_test_thrown_Exception() {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        UpdateSceneCaseConnRequest request = getUpdateRequest();
        assertThatThrownBy(() -> sceneCaseService.editConn(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the get method in the SceneCase service")
    void get_test() {
        List<SceneCaseEntity> sceneCaseList = Lists.newArrayList(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findAll(any(Example.class))).thenReturn(sceneCaseList);
        List<SceneCaseEntity> dto = sceneCaseService.get(MOCK_GROUP_ID, MOCK_PROJECT_ID);
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
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<ApiTestCaseEntity> apiTestCase = Optional.ofNullable(ApiTestCaseEntity.builder().build());
        when(apiTestCaseRepository.findById(any())).thenReturn(apiTestCase);
        when(sceneCaseApiRepository.insert(any(SceneCaseApiEntity.class)))
            .thenReturn(SceneCaseApiEntity.builder().build());
        Optional<ApiEntity> apiEntity = Optional.ofNullable(ApiEntity.builder().build());
        ApiTestCaseEntity testCase = ApiTestCaseEntity.builder().id(MOCK_ID).build();
        when(apiTestCaseMapper.toEntityByApiEntity(any())).thenReturn(testCase);
        when(sceneCaseApiRepository.insert(any(SceneCaseApiEntity.class)))
            .thenReturn(SceneCaseApiEntity.builder().build());
        AddSceneCaseApiByIdsRequest request = getAddRequest();
        Boolean isSuccess = sceneCaseService.addApi(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the addApi method in the SceneCase service thrown exception")
    void addApi_test_thrownException() {
        Optional<SceneCaseEntity> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        AddSceneCaseApiByIdsRequest request = getAddRequest();
        assertThatThrownBy(() -> sceneCaseService.addApi(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addApi method in the SceneCase service thrown exception")
    void addApi_test_thrown_Exception() {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        AddSceneCaseApiByIdsRequest request = getAddRequest();
        assertThatThrownBy(() -> sceneCaseService.addApi(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addTemplate method in the SceneCase service")
    void addTemplate_thenRight() {
        Optional<SceneCaseEntity> sceneCase = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        List<CaseTemplateApiEntity> caseTemplateApiList = Lists.newArrayList(CaseTemplateApiEntity.builder().build());
        when(caseTemplateApiService.listByCaseTemplateId(any())).thenReturn(caseTemplateApiList);
        List<CaseTemplateApiConn> caseTemplateApiConnList = Lists
            .newArrayList(CaseTemplateApiConn.builder().execute(Boolean.TRUE).caseTemplateApiId(MOCK_ID).build());
        when(sceneCaseMapper.toCaseTemplateApiConnList(any())).thenReturn(caseTemplateApiConnList);
        when(sceneCaseApiRepository.insert(any(SceneCaseApiEntity.class)))
            .thenReturn(SceneCaseApiEntity.builder().build());
        AddCaseTemplateConnRequest request = getAddConnRequest();
        Boolean isSuccess = sceneCaseService.addTemplate(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the addTemplate method in the SceneCase service thrown exception")
    void addTemplate_thenThrowException() {
        Optional<SceneCaseEntity> sceneCase = Optional.empty();
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        AddCaseTemplateConnRequest request = getAddConnRequest();
        assertThatThrownBy(() -> sceneCaseService.addTemplate(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the addTemplate method in the SceneCase service thrown exception")
    void addTemplate_thenThrow_Exception() {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        AddCaseTemplateConnRequest request = getAddConnRequest();
        assertThatThrownBy(() -> sceneCaseService.addTemplate(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the SceneCase service")
    void delete_thenRight() {
        when(customizedSceneCaseRepository.deleteByIds(any())).thenReturn(Boolean.TRUE);
        List<String> sceneCaseApiEntityList = Lists.newArrayList(MOCK_ID);
        when(customizedSceneCaseApiRepository.findSceneCaseApiIdsBySceneCaseIds(any()))
            .thenReturn(sceneCaseApiEntityList);
        when(customizedSceneCaseApiRepository.deleteByIds(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.delete(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the delete method in the SceneCase service thrown exception")
    void delete_thenRight_throwApiException() {
        when(customizedSceneCaseRepository.deleteByIds(any()))
            .thenThrow(new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.delete(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the delete method in the SceneCase service thrown exception")
    void delete_thenRight_throwException() {
        when(customizedSceneCaseRepository.deleteByIds(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sceneCaseService.delete(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the recover method in the SceneCase service")
    void recover_thenRight() {
        when(customizedSceneCaseRepository.recover(any())).thenReturn(Boolean.TRUE);
        List<String> sceneCaseApiEntityList = Lists.newArrayList(MOCK_ID);
        when(customizedSceneCaseApiRepository.findSceneCaseApiIdsBySceneCaseIds(any()))
            .thenReturn(sceneCaseApiEntityList);
        when(customizedSceneCaseApiRepository.recover(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.recover(Lists.newArrayList(MOCK_ID));
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the recover method in the SceneCase service thrown exception")
    void recover_thenRight_thrownApiException() {
        when(customizedSceneCaseRepository.recover(any()))
            .thenThrow(new ApiTestPlatformException(RECOVER_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.recover(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the recover method in the SceneCase service thrown exception")
    void recover_thenRight_thrownException() {
        when(customizedSceneCaseRepository.recover(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sceneCaseService.recover(Lists.newArrayList(MOCK_ID)))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getByApiId method in the SceneCase service")
    void getByApiId_thenRight() {
        List<SceneCaseApiEntity> entityList = Lists
            .newArrayList(SceneCaseApiEntity.builder().sceneCaseId(MOCK_ID).build());
        when(customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(any())).thenReturn(entityList);
        List<SceneCaseEntity> sceneCaseEntityList = Lists.newArrayList(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findByIdInAndRemoved(any(), any(Boolean.class))).thenReturn(sceneCaseEntityList);
        SceneCaseResponse response = SceneCaseResponse.builder().id(MOCK_ID).build();
        when(sceneCaseMapper.toDto(any())).thenReturn(response);
        List<SceneCaseResponse> responses = sceneCaseService.getByApiId(MOCK_ID);
        assertThat(responses.get(0).getId()).isEqualTo(sceneCaseEntityList.get(0).getId());
    }

    @Test
    @DisplayName("Test the getByApiId method in the SceneCase service")
    void getByApiId_thenNull() {
        List<SceneCaseApiEntity> entityList = Lists.newArrayList();
        when(customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(any())).thenReturn(entityList);
        List<SceneCaseResponse> responses = sceneCaseService.getByApiId(MOCK_ID);
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("Test the getByApiId method in the SceneCase service thrown exception")
    void getByApiId_thenRight_thrownException() {
        when(customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> sceneCaseService.getByApiId(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the removeRef method in the SceneCase service")
    void removeRef_thenRight() throws JsonProcessingException {
        String sceneCaseId = ObjectId.get().toString();
        String caseTemplateId = ObjectId.get().toString();
        String id = ObjectId.get().toString();
        int order = 1;
        when(sceneCaseApiRepository
            .findBySceneCaseIdAndOrderIsGreaterThanEqualAndRemovedIsFalseOrderByOrder(sceneCaseId, order))
            .thenReturn(
                List.of(SceneCaseApiEntity.builder().sceneCaseId(sceneCaseId).order(1).caseTemplateId(caseTemplateId)
                        .apiTestCase(ApiTestCaseEntity.builder().build())
                        .caseTemplateApiConnList(List.of(CaseTemplateApiConn.builder().caseTemplateApiId(id).build()))
                        .build(),
                    SceneCaseApiEntity.builder().order(2).build()));
        when(caseTemplateApiService.listResponseByCaseTemplateId(caseTemplateId))
            .thenReturn(List.of(CaseTemplateApiResponse.builder().id(ObjectId.get().toString()).build()));
        when(objectMapper.writeValueAsString(any())).thenReturn("[]");
        when(sceneCaseApiService.listBySceneCaseId(sceneCaseId)).thenReturn(Collections.emptyList());
        List<SceneCaseApiConnResponse> result = sceneCaseService
            .removeRef(sceneCaseId, caseTemplateId, order);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Test the copySteps method in the SceneCase service")
    void copySteps_thenRight() throws JsonProcessingException {
        Optional<SceneCaseEntity> sceneCaseEntity = Optional.ofNullable(SceneCaseEntity.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCaseEntity);
        List<SceneCaseApiEntity> sceneCaseApiEntityList =
            Lists.newArrayList(SceneCaseApiEntity.builder().id(MOCK_ID).order(MOCK_SIZE).aliasName(MOCK_NAME).build());
        when(sceneCaseApiRepository.findAllByIdIsIn(any())).thenReturn(sceneCaseApiEntityList);
        List<SceneCaseApiVo> sceneCaseApiResponseList =
            Lists.newArrayList(SceneCaseApiVo.builder().id(MOCK_ID).order(MOCK_SIZE).aliasName(MOCK_NAME).build());
        when(sceneCaseApiMapper.toSceneCaseApiVoList(any())).thenReturn(sceneCaseApiResponseList);
        CopyStepsRequest request = CopyStepsRequest.builder()
            .sceneCaseId(MOCK_ID)
            .projectId(MOCK_ID)
            .caseOrderList(Lists.newArrayList(CaseOrderRequest.builder().caseId(MOCK_ID).order(MOCK_PAGE).build()))
            .build();
        when(objectMapper.writeValueAsString(any())).thenReturn("[]");
        Boolean isSuccess = sceneCaseService.copySteps(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("An exception occurred while copy steps")
    void copySteps_thenJsonException() throws JsonProcessingException {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        CopyStepsRequest request = CopyStepsRequest.builder()
            .sceneCaseId(MOCK_ID)
            .projectId(MOCK_ID)
            .caseOrderList(Lists.newArrayList(CaseOrderRequest.builder().caseId(MOCK_ID).order(MOCK_PAGE).build()))
            .build();
        assertThatThrownBy(()->sceneCaseService.copySteps(request)).isInstanceOf(ApiTestPlatformException.class);
    }
    @Test
    @DisplayName("Test the review method in the SceneCase service")
    public void review_thenRight() {
        Optional<SceneCaseEntity> sceneCaseEntity = Optional.ofNullable(SceneCaseEntity.builder().id(MOCK_ID).build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCaseEntity);
        ReviewRequest request = ReviewRequest.builder().comment(MOCK_NAME).reviewStatus(MOCK_PAGE).build();
        Boolean isSuccess = sceneCaseService.review(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the review method in the SceneCase service")
    public void review_thenException() {
        when(sceneCaseRepository.findById(any())).thenThrow(new RuntimeException());
        ReviewRequest request = ReviewRequest.builder().comment(MOCK_NAME).reviewStatus(MOCK_PAGE).build();
        assertThatThrownBy(() -> sceneCaseService.review(request)).isInstanceOf(ApiTestPlatformException.class);
    }

    private AddCaseTemplateConnRequest getAddConnRequest() {
        return AddCaseTemplateConnRequest.builder()
            .sceneCaseId(MOCK_ID)
            .caseTemplateIds(Lists.newArrayList(AddCaseTemplateApi.builder().id(MOCK_ID).order(MOCK_SIZE).build()))
            .build();
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
                    .apiEntity(ApiRequest.builder().build())
                    .order(MOCK_PAGE)
                    .build()))
            .build();
    }

    private UpdateSceneCaseConnRequest getUpdateRequest() {
        return UpdateSceneCaseConnRequest.builder()
            .sceneCaseId(MOCK_ID)
            .sceneCaseApiRequest(
                Lists.newArrayList(
                    UpdateSceneCaseApiConnRequest.builder()
                        .apiTestCase(ApiTestCaseRequest.builder().execute(Boolean.TRUE).build())
                        .caseTemplateApiList(Lists.newArrayList(CaseTemplateApiResponse.builder().build()))
                        .build())
            ).build();
    }

}
