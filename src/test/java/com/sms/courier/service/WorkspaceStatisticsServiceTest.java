package com.sms.courier.service;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.impl.WorkspaceStatisticsServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for WorkspaceStatisticsService")
public class WorkspaceStatisticsServiceTest {

    private final ProjectService projectService = mock(ProjectService.class);
    private final CommonStatisticsRepository commonStatisticsRepository = mock(CommonStatisticsRepository.class);
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository = mock(
        CustomizedSceneCaseRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository =
        mock(CustomizedApiTestCaseRepository.class);
    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ProjectStatisticsService projectStatisticsService = mock(ProjectStatisticsService.class);
    private final WorkspaceStatisticsService workspaceStatisticsService =
        new WorkspaceStatisticsServiceImpl(projectService, commonStatisticsRepository, customizedSceneCaseRepository,
            customizedApiTestCaseRepository, customizedApiRepository, projectStatisticsService);

    private static final String ID = ObjectId.get().toString();
    private static final Integer MOCK_DAY = 7;
    private static final Long MOCK_COUNT = 1L;


    @Test
    @DisplayName("Test the caseAllCount method in the Workspace service")
    public void caseAllCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList();
        when(projectService.list(any())).thenReturn(projectResponses);
        when(customizedApiTestCaseRepository.count(any())).thenReturn(MOCK_COUNT);
        Long dto = workspaceStatisticsService.allCount(ID, StatisticsCountType.API.getName());
        assertThat(dto).isEqualTo(0L);
    }

    @Test
    @DisplayName("An exception occurred while caseAllCount Workspace")
    public void caseAllCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.allCount(ID, StatisticsCountType.API.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseGroupDayCount method in the Workspace service")
    public void caseGroupDayCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CountStatisticsResponse> responses = workspaceStatisticsService.groupDayCount(ID, MOCK_DAY,
            StatisticsGroupQueryType.API_TEST_CASE.getName());
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }


    @Test
    @DisplayName("An exception occurred while caseGroupDayCount Workspace")
    public void caseGroupDayCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupDayCount(ID, MOCK_DAY, StatisticsGroupQueryType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseGroupDayCount Workspace")
    public void caseGroupDayCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupDayCount(ID, MOCK_DAY, StatisticsGroupQueryType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }


    @Test
    @DisplayName("Test the caseGroupUserCount method in the Workspace service")
    public void caseGroupUserCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().build());
        when(commonStatisticsRepository.getGroupUserCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountUserStatisticsResponse> responses = workspaceStatisticsService
            .groupUserCount(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE.getName());
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while caseGroupUserCount Workspace")
    public void caseGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupUserCount(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseGroupUserCount Workspace")
    public void caseGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupUserCount(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }


    @Test
    @DisplayName("Test the caseJobGroupUserCount method in the Workspace service")
    public void caseJobGroupUserCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().build());
        when(commonStatisticsRepository.getGroupUserCountByJob(any(), any(), eq(ApiTestCaseJobEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountUserStatisticsResponse> responses = workspaceStatisticsService
            .groupUserByJob(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE_JOB.getName());
        assertThat(responses).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupUserCount Workspace")
    public void caseJobGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupUserByJob(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE_JOB.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupUserCount Workspace")
    public void caseJobGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService
            .groupUserByJob(MOCK_DAY, ID, StatisticsGroupQueryType.API_TEST_CASE_JOB.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the projectCasePercentage method in the Workspace service")
    public void projectCasePercentage_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList();
        when(projectService.list(any())).thenReturn(projectResponses);
        when(projectStatisticsService.caseCount(any())).thenReturn(MOCK_COUNT);
        when(projectStatisticsService.apiAllCount(any())).thenReturn(MOCK_COUNT);
        List<WorkspaceProjectCaseStatisticsResponse> dto = workspaceStatisticsService.projectPercentage(ID,
            StatisticsCountType.API_TEST_CASE.getName());
        assertThat(dto).isEmpty();
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectCasePercentage_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR));
        assertThatThrownBy(
            () -> workspaceStatisticsService.projectPercentage(ID, StatisticsCountType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectCasePercentage_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(
            () -> workspaceStatisticsService.projectPercentage(ID, StatisticsCountType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
