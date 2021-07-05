package com.sms.satp.service;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateConnResponse;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateConnMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.impl.SceneCaseServiceImpl;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_CONN_ERROR;
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
    private CaseTemplateConnService caseTemplateConnService;
    private CaseTemplateConnMapper caseTemplateConnMapper;
    private CaseTemplateApiService caseTemplateApiService;

    private final static String MOCK_ID = new ObjectId().toString();
    private final static String MOCK_NAME = "test";
    private final static String MOCK_PROJECT_ID = "1";
    private final static String MOCK_GROUP_ID = "1";
    private final static Long MOCK_CREATE_USER_ID = 1L;
    private final static Integer MOCK_PAGE = 1;
    private final static Integer MOCK_SIZE = 1;
    private final static long MOCK_TOTAL = 1L;

    @BeforeEach
    void setBean() {
        sceneCaseRepository = mock(SceneCaseRepository.class);
        customizedSceneCaseRepository = mock(CustomizedSceneCaseRepository.class);
        sceneCaseMapper = mock(SceneCaseMapper.class);
        sceneCaseApiService = mock(SceneCaseApiService.class);
        caseTemplateConnService = mock(CaseTemplateConnService.class);
        caseTemplateConnMapper = mock(CaseTemplateConnMapper.class);
        caseTemplateApiService = mock(CaseTemplateApiService.class);
        sceneCaseService = new SceneCaseServiceImpl(sceneCaseRepository, customizedSceneCaseRepository,
            sceneCaseMapper, sceneCaseApiService,
            caseTemplateConnService, caseTemplateConnMapper, caseTemplateApiService);
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
        List<CaseTemplateConn> caseTemplateConnList = Lists.newArrayList(CaseTemplateConn.builder().build());
        when(caseTemplateConnService.listBySceneCaseId(any())).thenReturn(caseTemplateConnList);
        when(caseTemplateConnService.deleteById(any())).thenReturn(Boolean.TRUE);
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
        List<CaseTemplateConn> caseTemplateConnList =
            Lists.newArrayList(CaseTemplateConn.builder().id(MOCK_ID).build());
        when(caseTemplateConnService.listBySceneCaseId(any(), anyBoolean())).thenReturn(caseTemplateConnList);
        when(caseTemplateConnService.edit(any())).thenReturn(Boolean.TRUE);
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
        List<SceneCase> dtoList = Lists.newArrayList(SceneCase.builder().build());
        Pageable pageable = PageRequest.of(MOCK_PAGE, MOCK_SIZE);
        Page<SceneCase> sceneCaseDtoPage = new PageImpl<>(dtoList, pageable, MOCK_TOTAL);
        when(customizedSceneCaseRepository.search(any(), any())).thenReturn(sceneCaseDtoPage);
        Page<SceneCaseResponse> pageDto = sceneCaseService.page(dto, MOCK_PROJECT_ID);
        assertThat(pageDto).isNotNull();
    }

    @Test
    @DisplayName("Test the search method in the SceneCase service thrown exception")
    void search_test_thenThrownException() {
        SearchSceneCaseRequest dto = new SearchSceneCaseRequest();
        dto.setName(MOCK_NAME);
        when(customizedSceneCaseRepository.search(any(), any()))
            .thenThrow(new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR));
        assertThatThrownBy(() -> sceneCaseService.page(dto, MOCK_PROJECT_ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the getConn method in the SceneCase service")
    void getConn_test() {
        Optional<SceneCase> optional = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(optional);
        SceneCaseResponse sceneCaseDto = SceneCaseResponse.builder().build();
        when(sceneCaseMapper.toDto(any())).thenReturn(sceneCaseDto);
        List<SceneCaseApiResponse> sceneCaseApiDtoList = Lists.newArrayList();
        when(sceneCaseApiService.listBySceneCaseId(any(), anyBoolean())).thenReturn(sceneCaseApiDtoList);
        List<CaseTemplateConn> caseTemplateConnList =
            Lists.newArrayList(CaseTemplateConn.builder().id(MOCK_ID).build());
        when(caseTemplateConnService.listBySceneCaseId(any(), anyBoolean())).thenReturn(caseTemplateConnList);
        CaseTemplateConnResponse connDto = CaseTemplateConnResponse.builder().build();
        when(caseTemplateConnMapper.toCaseTemplateConnDto(any())).thenReturn(connDto);
        when(caseTemplateApiService.listByCaseTemplateId(any(), anyBoolean())).thenReturn(Lists.newArrayList());
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
        when(sceneCaseApiService.batchEdit(any())).thenReturn(Boolean.TRUE);
        List<CaseTemplateConn> caseTemplateConnList = Lists
            .newArrayList(CaseTemplateConn.builder().id(MOCK_ID).build());
        when(caseTemplateConnMapper.toCaseTemplateConnList(any())).thenReturn(caseTemplateConnList);
        when(caseTemplateConnService.editList(any())).thenReturn(Boolean.TRUE);
        Boolean isSuccess = sceneCaseService.editConn(UpdateSceneTemplateRequest.builder()
            .updateCaseTemplateConnRequests(Lists.newArrayList(UpdateCaseTemplateConnRequest.builder().build()))
            .updateSceneCaseApiRequests(Lists.newArrayList(UpdateSceneCaseApiRequest.builder().build())).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the editConn method in the SceneCase service thrown exception")
    void editConn_test_thrownException() {
        when(sceneCaseApiService.batchEdit(any())).thenReturn(Boolean.TRUE);
        when(caseTemplateConnMapper.toCaseTemplateConnList(any()))
            .thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_CONN_ERROR));
        when(caseTemplateConnService.editList(any())).thenReturn(Boolean.TRUE);
        assertThatThrownBy(() -> sceneCaseService.editConn(UpdateSceneTemplateRequest.builder()
            .updateCaseTemplateConnRequests(Lists.newArrayList(UpdateCaseTemplateConnRequest.builder().build()))
            .updateSceneCaseApiRequests(Lists.newArrayList(UpdateSceneCaseApiRequest.builder().build())).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }
}
