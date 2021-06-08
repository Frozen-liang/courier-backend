package com.sms.satp.service;

import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.engine.service.CaseDispatcherService;
import com.sms.satp.entity.datacollection.DataCollection;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.mapper.SceneCaseJobMapper;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import com.sms.satp.repository.CustomizedSceneCaseJobRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseJobRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.impl.SceneCaseJobServiceImpl;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

@DisplayName("Test cases for SceneCaseJobServiceTest")
class SceneCaseJobServiceTest {

    private final SceneCaseApiRepository sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final SceneCaseRepository sceneCaseRepository = mock(SceneCaseRepository.class);
    private final CaseTemplateConnRepository caseTemplateConnRepository = mock(CaseTemplateConnRepository.class);
    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final CaseTemplateApiMapper caseTemplateApiMapper = mock(CaseTemplateApiMapper.class);
    private final SceneCaseJobRepository sceneCaseJobRepository = mock(SceneCaseJobRepository.class);
    private final DataCollectionMapper dataCollectionMapper = mock(DataCollectionMapper.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        mock(CustomizedSceneCaseJobRepository.class);
    private final SceneCaseJobMapper sceneCaseJobMapper = mock(SceneCaseJobMapper.class);
    private final CaseDispatcherService caseDispatcherService = mock(CaseDispatcherService.class);
    private final ProjectEnvironmentMapper projectEnvironmentMapper = mock(ProjectEnvironmentMapper.class);
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository =
        mock(CustomizedCaseTemplateApiRepository.class);
    private final SceneCaseJobService sceneCaseJobService = new SceneCaseJobServiceImpl(sceneCaseApiRepository,
        projectEnvironmentService, sceneCaseRepository, caseTemplateConnRepository, sceneCaseJobRepository,
        dataCollectionMapper, customizedSceneCaseJobRepository, sceneCaseJobMapper, projectEnvironmentMapper,
        caseDispatcherService, customizedCaseTemplateApiRepository);

    private final static String MOCK_ID = "1";
    private final static Integer MOCK_NUM = 1;

    @Test
    @DisplayName("Test the add method in the SceneCaseJob service")
    void add_test() {
        ProjectEnvironment environment = ProjectEnvironment.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        Optional<SceneCaseApi> sceneCaseApi = Optional.ofNullable(SceneCaseApi.builder().build());
        when(sceneCaseApiRepository.findById(any())).thenReturn(sceneCaseApi);
        Optional<CaseTemplateConn> caseTemplateConn = Optional
            .ofNullable(CaseTemplateConn.builder().caseTemplateApiConnList(
                Lists.newArrayList(CaseTemplateApiConn.builder().caseTemplateApiId(MOCK_ID).order(MOCK_NUM).build()))
                .build());
        when(caseTemplateConnRepository.findById(any())).thenReturn(caseTemplateConn);
        List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList(CaseTemplateApi.builder().id(MOCK_ID).build());
        when(caseTemplateApiRepository.findAll(any(Example.class))).thenReturn(caseTemplateApiList);
        JobDataCollection dataCollection1 = JobDataCollection.builder().build();
        when(dataCollectionMapper.toDataCollectionJob(any(), any())).thenReturn(dataCollection1);
        List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList();
        when(caseTemplateApiMapper.toSceneCaseList(any())).thenReturn(sceneCaseApiList);
        AddSceneCaseJobRequest request = getAddRequest();
        Boolean isSuccess = sceneCaseJobService.add(request);
        assertTrue(isSuccess);
    }

    /*@Test
    @DisplayName("Test the add method in the SceneCaseJob service")
    void add_test_DataCollectionIsNull() {
        ProjectEnvironment environment = ProjectEnvironment.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        Optional<SceneCase> sceneCase = Optional.ofNullable(SceneCase.builder().build());
        when(sceneCaseRepository.findById(any())).thenReturn(sceneCase);
        SceneCaseJob sceneCaseJob = SceneCaseJob.builder().id(MOCK_ID).build();
        when(sceneCaseJobRepository.insert(any(SceneCaseJob.class))).thenReturn(sceneCaseJob);
        AddSceneCaseJobRequest request = getAddRequest();
        request.setDataCollection(null);
        Boolean isSuccess = sceneCaseJobService.add(request);
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseJob service thrown exception")
    void add_test_EnvironmentIsNull() {
        when(projectEnvironmentService.findOne(any())).thenReturn(null);
        assertThatThrownBy(() -> sceneCaseJobService.add(getAddRequest()));
    }

    @Test
    @DisplayName("Test the add method in the SceneCaseJob service thrown exception")
    void add_test_SceneCaseIsNull() {
        ProjectEnvironment environment = ProjectEnvironment.builder().build();
        when(projectEnvironmentService.findOne(any())).thenReturn(environment);
        when(sceneCaseRepository.findById(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> sceneCaseJobService.add(getAddRequest()));
    }*/

    @Test
    @DisplayName("Test the page method in the SceneCaseJob service")
    void page_test() {
        Page<SceneCaseJob> sceneCaseJobPage = Page.empty(Pageable.unpaged());
        when(customizedSceneCaseJobRepository.page(any(), any())).thenReturn(sceneCaseJobPage);
        Page<SceneCaseJob> responsePage = sceneCaseJobService.page(Lists.newArrayList(MOCK_ID),
            PageDto.builder().build());
        assertThat(responsePage).isNotNull();
    }

    @Test
    @DisplayName("Test the page method in the SceneCaseJob service thrown exception")
    void page_test_thrownException() {
        when(customizedSceneCaseJobRepository.page(any(), any()))
            .thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_JOB_PAGE_ERROR));
        assertThatThrownBy(() -> sceneCaseJobService.page(Lists.newArrayList(MOCK_ID),
            PageDto.builder().build()));
    }

    @Test
    @DisplayName("Test the get method in the SceneCaseJob service")
    void get_test() {
        Optional<SceneCaseJob> sceneCaseJob = Optional.ofNullable(SceneCaseJob.builder().id(MOCK_ID).build());
        when(sceneCaseJobRepository.findOne(any())).thenReturn(sceneCaseJob);
        SceneCaseJob dto = sceneCaseJobService.get(MOCK_ID);
        assertThat(dto).isNotNull();
    }

    @Test
    @DisplayName("Test the get method in the SceneCaseJob service thrown exception")
    void get_test_thrownException() {
        when(sceneCaseJobRepository.findOne(any())).thenThrow(new ApiTestPlatformException(GET_SCENE_CASE_JOB_ERROR));
        assertThatThrownBy(() -> sceneCaseJobService.get(MOCK_ID)).isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseJob service")
    void edit_test() {
        SceneCaseJob job = SceneCaseJob.builder().id(MOCK_ID).build();
        when(sceneCaseJobRepository.save(any())).thenReturn(job);
        Boolean isSuccess = sceneCaseJobService.edit(SceneCaseJob.builder().id(MOCK_ID).build());
        assertTrue(isSuccess);
    }

    @Test
    @DisplayName("Test the edit method in the SceneCaseJob service thrown exception")
    void edit_test_thrownException() {
        when(sceneCaseJobRepository.save(any())).thenThrow(new ApiTestPlatformException(EDIT_SCENE_CASE_JOB_ERROR));
        assertThatThrownBy(() -> sceneCaseJobService.edit(SceneCaseJob.builder().id(MOCK_ID).build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    private AddSceneCaseJobRequest getAddRequest() {
        return AddSceneCaseJobRequest.builder()
            .sceneCaseId(MOCK_ID)
            .sceneCaseApiIds(Lists.newArrayList(MOCK_ID))
            .caseTemplateConnIds(Lists.newArrayList(MOCK_ID))
            .envId(MOCK_ID)
            .dataCollection(
                DataCollection.builder().id(MOCK_ID).dataList(Lists.newArrayList(TestData.builder().build())).build())
            .build();
    }

}
