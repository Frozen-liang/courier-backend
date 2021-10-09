package com.sms.courier.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.datacollection.DataParam;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.JobSceneCaseApi;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.job.common.CaseReport;
import com.sms.courier.entity.job.common.JobApiTestCase;
import com.sms.courier.entity.job.common.RunningJobAck;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.entity.schedule.CaseCondition;
import com.sms.courier.entity.schedule.ScheduleEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import com.sms.courier.mapper.JobMapper;
import com.sms.courier.mapper.JobMapperImpl;
import com.sms.courier.mapper.MatchParamInfoMapperImpl;
import com.sms.courier.mapper.ParamInfoMapper;
import com.sms.courier.mapper.ParamInfoMapperImpl;
import com.sms.courier.mapper.ResponseResultVerificationMapperImpl;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CaseTemplateApiRepository;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.repository.ScheduleSceneCaseJobRepository;
import com.sms.courier.service.impl.ScheduleSceneCaseJobServiceImpl;
import com.sms.courier.utils.ExceptionUtils;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("Tests for ScheduleSceneCaseJobService")
class ScheduleSceneCaseJobServiceTest {

    private final ScheduleSceneCaseJobRepository scheduleSceneCaseJobRepository = mock(
        ScheduleSceneCaseJobRepository.class);
    private final CaseDispatcherService caseDispatcherService = mock(CaseDispatcherService.class);
    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final ApiTestCaseService apiTestCaseService = mock(ApiTestCaseService.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final SceneCaseRepository sceneCaseRepository = mock(SceneCaseRepository.class);
    private final SceneCaseApiRepository sceneCaseApiRepository = mock(SceneCaseApiRepository.class);
    private final CaseTemplateApiRepository caseTemplateApiRepository = mock(CaseTemplateApiRepository.class);
    private final ScheduleRecordRepository scheduleRecordRepository = mock(ScheduleRecordRepository.class);
    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));
    private final ScheduleSceneCaseJobService scheduleSceneCaseJobService =
        new ScheduleSceneCaseJobServiceImpl(scheduleSceneCaseJobRepository, jobMapper, caseDispatcherService,
            projectEnvironmentService, commonRepository, sceneCaseRepository, sceneCaseApiRepository,
            caseTemplateApiRepository, scheduleRecordRepository, applicationEventPublisher);
    private final ScheduleSceneCaseJobEntity scheduleCaseJob =
        ScheduleSceneCaseJobEntity.builder().id(ID)
            .apiTestCase(List.of(JobSceneCaseApi.builder().jobApiTestCase(JobApiTestCase.builder().build()).build()))
            .build();

    private final SceneCaseJobReport sceneCaseJobReport =
        SceneCaseJobReport.builder()
            .jobType(JobType.SCHEDULER_SCENE_CASE)
            .caseReportList(List.of(CaseReport.builder().build()))
            .jobId(ObjectId.get().toString()).build();
    private final SceneCaseEntity sceneCaseEntity = SceneCaseEntity.builder().id(ID).dataCollId(ID).build();
    private static final String ID = ObjectId.get().toString();
    private static final String ENGINE_ID = "/engine/13/invoke";
    private static final List<String> ENGINE_ID_LIST = Collections.singletonList(ENGINE_ID);


    @Test
    @DisplayName("Test the handleJobReport method in the ScheduleSceneCaseJob service")
    public void handleJobReport_test() {
        when(scheduleSceneCaseJobRepository.findById(any())).thenReturn(Optional.of(scheduleCaseJob));
        when(scheduleSceneCaseJobRepository.save(any(ScheduleSceneCaseJobEntity.class))).thenReturn(scheduleCaseJob);
        doNothing().when(caseDispatcherService).sendJobReport(anyString(), any(ApiTestCaseJobReportResponse.class));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        doNothing().when(apiTestCaseService).insertTestResult(anyString(), any());
        doNothing().when(applicationEventPublisher).publishEvent(any());
        scheduleSceneCaseJobService.handleJobReport(sceneCaseJobReport);
        verify(scheduleSceneCaseJobRepository, times(1)).save(any(ScheduleSceneCaseJobEntity.class));
    }


    @Test
    @DisplayName("Test the reallocateJob method in the ScheduleSceneCaseJob service")
    public void reallocateJob_test() {
        when(scheduleSceneCaseJobRepository.findByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(scheduleCaseJob));
        when(caseDispatcherService.dispatch(any(SceneCaseJobResponse.class))).thenReturn(ENGINE_ID);
        scheduleSceneCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(caseDispatcherService, times(1)).dispatch(any(SceneCaseJobResponse.class));
    }

    @Test
    @DisplayName("An apiTestPlatformException occurred while run reallocateJob in ScheduleSceneCaseJob service")
    public void reallocateJob_ApiTestPlatformException_test() {
        when(scheduleSceneCaseJobRepository.findByEngineIdInAndJobStatus(ENGINE_ID_LIST, JobStatus.RUNNING))
            .thenReturn(Collections.singletonList(scheduleCaseJob));
        doNothing().when(applicationEventPublisher).publishEvent(any());
        when(caseDispatcherService.dispatch(any(SceneCaseJobResponse.class)))
            .thenThrow(ExceptionUtils.mpe(ErrorCode.EXECUTE_API_TEST_CASE_ERROR));
        scheduleSceneCaseJobService.reallocateJob(ENGINE_ID_LIST);
        verify(scheduleSceneCaseJobRepository, times(1)).save(any(ScheduleSceneCaseJobEntity.class));
    }

    @Test
    @DisplayName("Test the runningJobAck method in the ScheduleSceneCaseJob service")
    public void runningJobAck_test() {
        when(commonRepository.updateFieldById(anyString(), any(), any())).thenReturn(true);
        RunningJobAck runningJobAck = new RunningJobAck();
        runningJobAck.setDestination(ENGINE_ID);
        runningJobAck.setJobId(ID);
        scheduleSceneCaseJobService.runningJobAck(runningJobAck);
        verify(commonRepository, times(1)).updateFieldById(anyString(), any(), any());
    }

    @ParameterizedTest
    @DisplayName("Test the schedule method in the ScheduleSceneCaseJob service")
    @ValueSource(strings = {"ALL", "PRIORITY_AND_TAG", "CUSTOM"})
    public void schedule_test_when_data_collection_is_not_empty(String caseFilter) {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envId(ObjectId.get().toString())
            .caseFilter(CaseFilter.valueOf(caseFilter))
            .caseCondition(CaseCondition.builder().tag(Collections.singletonList(ID)).build())
            .caseType(CaseType.SCENE_CASE).build();
        List<SceneCaseEntity> apiTestCaseEntities = Collections.singletonList(sceneCaseEntity);
        when(sceneCaseRepository.findByIdIn(any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndRemovedIsFalse(any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndTagIdIn(any(), any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndTagIdInAndPriorityIn(any(), any(), any())).thenReturn(apiTestCaseEntities);
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        List<SceneCaseApiEntity> sceneCaseApiList1 = getSceneCaseApiList();
        when(sceneCaseApiRepository.findSceneCaseApiEntitiesBySceneCaseIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(sceneCaseApiList1);
        when(caseTemplateApiRepository.findAllByCaseTemplateIdAndRemovedOrderByOrder(any(), anyBoolean()))
            .thenReturn(Collections.emptyList());
        when(commonRepository.findById(ID, DataCollectionEntity.class))
            .thenReturn(Optional.of(DataCollectionEntity.builder().dataList(List.of(
                TestData.builder().dataName("name").data(List.of(DataParam.builder().build())).build())).build()));
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(scheduleSceneCaseJobRepository.saveAll(any())).thenReturn(Collections.emptyList());
        scheduleSceneCaseJobService.schedule(schedule);
        verify(scheduleSceneCaseJobRepository, times(1)).saveAll(any());
    }

    @ParameterizedTest
    @DisplayName("Test the schedule method in the ScheduleSceneCaseJob service")
    @ValueSource(strings = {"ALL", "PRIORITY_AND_TAG", "CUSTOM"})
    public void schedule_test_when_data_collection_is_empty(String caseFilter) {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envId(ObjectId.get().toString())
            .caseFilter(CaseFilter.valueOf(caseFilter))
            .caseCondition(CaseCondition.builder().priority(List.of(1)).build()).caseType(CaseType.SCENE_CASE).build();
        List<SceneCaseEntity> apiTestCaseEntities = Collections.singletonList(sceneCaseEntity);
        when(sceneCaseRepository.findByIdIn(any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndRemovedIsFalse(any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndPriorityIn(any(), any())).thenReturn(apiTestCaseEntities);
        when(sceneCaseRepository.findByProjectIdAndTagIdInAndPriorityIn(any(), any(), any())).thenReturn(apiTestCaseEntities);
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        when(commonRepository.findById(ID, DataCollectionEntity.class)).thenReturn(Optional.empty());
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(scheduleSceneCaseJobRepository.saveAll(any())).thenReturn(Collections.emptyList());
        scheduleSceneCaseJobService.schedule(schedule);
        verify(scheduleSceneCaseJobRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the schedule method in the ScheduleSceneCaseJob service")
    public void schedule_test_when_scene_case_is_empty() {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envId(ObjectId.get().toString())
            .caseFilter(CaseFilter.CUSTOM)
            .caseCondition(CaseCondition.builder().build()).caseType(CaseType.CASE).build();
        when(apiTestCaseRepository.findByIdIn(any())).thenReturn(Collections.emptyList());
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        when(commonRepository.findById(ID, DataCollectionEntity.class)).thenReturn(Optional.empty());
        when(caseDispatcherService.dispatch(any(ApiTestCaseJobResponse.class))).thenReturn(ENGINE_ID);
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(commonRepository.updateField(any(), any(), any())).thenReturn(true);
        scheduleSceneCaseJobService.schedule(schedule);
        verify(commonRepository, times(1)).updateField(any(), any(), any());
    }

    private List<SceneCaseApiEntity> getSceneCaseApiList() {
        return Lists.newArrayList(
            SceneCaseApiEntity.builder()
                .id(ID)
                .order(Math.subtractExact(1, 20))
                .apiTestCase(ApiTestCaseEntity.builder().id(ID).execute(Boolean.TRUE).build())
                .build(),
            SceneCaseApiEntity.builder()
                .caseTemplateId(ID)
                .order(Math.subtractExact(1, 20))
                .apiTestCase(ApiTestCaseEntity.builder().id(ID).execute(Boolean.TRUE).build())
                .caseTemplateApiConnList(
                    Lists.newArrayList(CaseTemplateApiConn.builder().caseTemplateApiId(ID).execute(Boolean.TRUE)
                        .lock(Boolean.TRUE)
                        .build()))
                .build());

    }

}