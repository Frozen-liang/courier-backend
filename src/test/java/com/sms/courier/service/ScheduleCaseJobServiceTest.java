package com.sms.courier.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.CaseFilter;
import com.sms.courier.common.enums.CaseType;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.service.CaseDispatcherService;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.datacollection.DataCollectionEntity;
import com.sms.courier.entity.datacollection.DataParam;
import com.sms.courier.entity.datacollection.TestData;
import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.JobCaseApi;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.common.JobApiTestCase;
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
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.repository.ScheduleCaseJobRepository;
import com.sms.courier.repository.ScheduleRecordRepository;
import com.sms.courier.service.impl.ScheduleCaseJobServiceImpl;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.ApplicationEventPublisher;

@DisplayName("Tests for ScheduleCaseJobService")
class ScheduleCaseJobServiceTest {

    private final ScheduleCaseJobRepository scheduleCaseJobRepository = mock(ScheduleCaseJobRepository.class);
    private final CaseDispatcherService caseDispatcherService = mock(CaseDispatcherService.class);
    private final ProjectEnvironmentService projectEnvironmentService = mock(ProjectEnvironmentService.class);
    private final ApiTestCaseService apiTestCaseService = mock(ApiTestCaseService.class);
    private final CommonRepository commonRepository = mock(CommonRepository.class);
    private final ScheduleRecordRepository scheduleRecordRepository = mock(ScheduleRecordRepository.class);
    private final ApiTestCaseRepository apiTestCaseRepository = mock(ApiTestCaseRepository.class);
    private final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private final EngineJobManagement engineJobManagement = mock(EngineJobManagement.class);
    private final DatabaseService dataBaseService = mock(DatabaseService.class);
    private final ParamInfoMapper paramInfoMapper = new ParamInfoMapperImpl();
    private final JobMapper jobMapper = new JobMapperImpl(paramInfoMapper, new MatchParamInfoMapperImpl(),
        new ResponseResultVerificationMapperImpl(new MatchParamInfoMapperImpl()));
    private final ScheduleCaseJobService scheduleCaseJobService =
        new ScheduleCaseJobServiceImpl(scheduleCaseJobRepository, jobMapper, caseDispatcherService,
            projectEnvironmentService, commonRepository, apiTestCaseRepository, scheduleRecordRepository,
            applicationEventPublisher, engineJobManagement, dataBaseService);
    private final ScheduleCaseJobEntity scheduleCaseJob =
        ScheduleCaseJobEntity.builder().id(ID)
            .apiTestCase(JobCaseApi.builder().jobApiTestCase(JobApiTestCase.builder().build()).build()).build();

    private final ApiTestCaseJobReport caseJobReport =
        ApiTestCaseJobReport.builder().jobType(JobType.SCHEDULE_CATE).jobId(ObjectId.get().toString()).build();
    private final ApiTestCaseEntity apiTestCaseEntity = ApiTestCaseEntity.builder().id(ID).dataCollId(ID).caseName(
        "name").build();
    private static final String ID = ObjectId.get().toString();
    private static final String ENGINE_ID = "/engine/13/invoke";
    private static final String METADATA = "metadata";
    private static final List<String> ENGINE_ID_LIST = Collections.singletonList(ENGINE_ID);


    @Test
    @DisplayName("Test the handleJobReport method in the ScheduleCaseJob service")
    public void handleJobReport_test() {
        when(scheduleCaseJobRepository.findById(any())).thenReturn(Optional.of(scheduleCaseJob));
        when(scheduleCaseJobRepository.save(any(ScheduleCaseJobEntity.class))).thenReturn(scheduleCaseJob);
        doNothing().when(caseDispatcherService).sendJobReport(anyString(), any(ApiTestCaseJobReportResponse.class));
        doNothing().when(engineJobManagement).dispatcherJob(any(ScheduleCaseJobEntity.class));
        doNothing().when(apiTestCaseService).insertTestResult(anyString(), any());
        doNothing().when(applicationEventPublisher).publishEvent(any());
        scheduleCaseJobService.handleJobReport(caseJobReport);
        verify(scheduleCaseJobRepository, times(1)).save(any(ScheduleCaseJobEntity.class));
    }


    @ParameterizedTest
    @DisplayName("Test the schedule method in the ScheduleCaseJob service")
    @ValueSource(strings = {"ALL", "PRIORITY_AND_TAG", "CUSTOM"})
    public void schedule_test_when_data_collection_is_not_empty(String caseFilter) {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envIds(
            Lists.newArrayList(ObjectId.get().toString()))
            .caseFilter(CaseFilter.valueOf(caseFilter))
            .caseCondition(CaseCondition.builder().build()).caseType(CaseType.CASE).build();
        List<ApiTestCaseEntity> apiTestCaseEntities = Collections.singletonList(apiTestCaseEntity);
        when(apiTestCaseRepository.findByIdIn(any(List.class))).thenReturn(apiTestCaseEntities);
        when(apiTestCaseRepository.findByProjectIdIsAndRemovedIsFalse(any())).thenReturn(apiTestCaseEntities);
        when(apiTestCaseRepository.findByTagIdInAndProjectId(any(), any())).thenReturn(apiTestCaseEntities);
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        when(commonRepository.findById(ID, DataCollectionEntity.class))
            .thenReturn(Optional.of(DataCollectionEntity.builder().dataList(List.of(
                TestData.builder().dataName("name").data(List.of(DataParam.builder().build())).build())).build()));
        doNothing().when(engineJobManagement).dispatcherJob(any(ScheduleCaseJobEntity.class));
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(scheduleCaseJobRepository.saveAll(any())).thenReturn(Collections.emptyList());
        scheduleCaseJobService.schedule(schedule,METADATA);
        verify(scheduleCaseJobRepository, times(1)).saveAll(any());
    }

    @ParameterizedTest
    @DisplayName("Test the schedule method in the ScheduleCaseJob service")
    @ValueSource(strings = {"ALL", "PRIORITY_AND_TAG", "CUSTOM"})
    public void schedule_test_when_data_collection_is_empty(String caseFilter) {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envIds(
            Lists.newArrayList(ObjectId.get().toString()))
            .caseFilter(CaseFilter.valueOf(caseFilter))
            .caseCondition(CaseCondition.builder().build()).caseType(CaseType.CASE).build();
        List<ApiTestCaseEntity> apiTestCaseEntities = Collections.singletonList(apiTestCaseEntity);
        when(apiTestCaseRepository.findByIdIn(any(List.class))).thenReturn(apiTestCaseEntities);
        when(apiTestCaseRepository.findByProjectIdIsAndRemovedIsFalse(any())).thenReturn(apiTestCaseEntities);
        when(apiTestCaseRepository.findByTagIdInAndProjectId(any(), any())).thenReturn(apiTestCaseEntities);
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        when(commonRepository.findById(ID, DataCollectionEntity.class)).thenReturn(Optional.empty());
        doNothing().when(engineJobManagement).dispatcherJob(any(ScheduleCaseJobEntity.class));
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(scheduleCaseJobRepository.saveAll(any())).thenReturn(Collections.emptyList());
        scheduleCaseJobService.schedule(schedule,METADATA);
        verify(scheduleCaseJobRepository, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("Test the schedule method in the ScheduleCaseJob service")
    public void schedule_test_when_api_test_case_is_empty() {
        ScheduleEntity schedule = ScheduleEntity.builder().caseIds(List.of(ID)).envIds(
            Lists.newArrayList(ObjectId.get().toString()))
            .caseFilter(CaseFilter.CUSTOM)
            .caseCondition(CaseCondition.builder().build()).caseType(CaseType.CASE).build();
        when(apiTestCaseRepository.findByIdIn(any(List.class))).thenReturn(Collections.emptyList());
        when(projectEnvironmentService.findOne(anyString())).thenReturn(ProjectEnvironmentEntity.builder().build());
        when(commonRepository.findById(ID, DataCollectionEntity.class)).thenReturn(Optional.empty());
        doNothing().when(engineJobManagement).dispatcherJob(any(ScheduleCaseJobEntity.class));
        when(scheduleRecordRepository.save(any())).thenReturn(ScheduleRecordEntity.builder().build());
        when(commonRepository.updateField(any(), any(), any())).thenReturn(true);
        scheduleCaseJobService.schedule(schedule,METADATA);
        verify(commonRepository, times(1)).updateField(any(), any(), any());
    }

}