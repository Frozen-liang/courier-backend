package com.sms.courier.service;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.impl.WorkspaceStatisticsServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_PROJECT_SCENE_CASE_PERCENTAGE_ERROR;
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
    @DisplayName("Test the caseGroupDayCount method in the Workspace service")
    public void caseGroupDayCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService.caseGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("Test the caseGroupDayCount method in the Workspace service")
    public void caseGroupDayCount_projectIsNull_test() {
        when(projectService.list(any())).thenReturn(Lists.newArrayList());
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService.caseGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while caseGroupDayCount Workspace")
    public void caseGroupDayCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.caseGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseGroupDayCount Workspace")
    public void caseGroupDayCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.caseGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneAllCount method in the Workspace service")
    public void sceneAllCount_projectIsNull_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(customizedSceneCaseRepository.count(any())).thenReturn(MOCK_COUNT);
        Long dto = workspaceStatisticsService.sceneAllCount(ID);
        assertThat(dto).isEqualTo(1);
    }

    @Test
    @DisplayName("An exception occurred while sceneAllCount Workspace")
    public void sceneAllCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_SCENE_COUNT_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.sceneAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseAllCount method in the Workspace service")
    public void caseAllCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(customizedApiTestCaseRepository.count(any())).thenReturn(MOCK_COUNT);
        Long dto = workspaceStatisticsService.caseAllCount(ID);
        assertThat(dto).isEqualTo(MOCK_COUNT);
    }

    @Test
    @DisplayName("An exception occurred while caseAllCount Workspace")
    public void caseAllCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.caseAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the apiAllCount method in the Workspace service")
    public void apiAllCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(customizedApiRepository.count(any())).thenReturn(MOCK_COUNT);
        Long dto = workspaceStatisticsService.apiAllCount(ID);
        assertThat(dto).isEqualTo(MOCK_COUNT);
    }

    @Test
    @DisplayName("An exception occurred while apiAllCount Workspace")
    public void apiAllCount_exception_test() {
        when(projectService.list(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.apiAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneCaseGroupDayCount method in the Workspace service")
    public void sceneCaseGroupDayCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService.sceneCaseGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("Test the sceneCaseGroupDayCount method in the Workspace service")
    public void sceneCaseGroupDayCount_projectIsNull_test() {
        when(projectService.list(any())).thenReturn(Lists.newArrayList());
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService.sceneCaseGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseGroupDayCount Workspace")
    public void sceneCaseGroupDayCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseGroupDayCount Workspace")
    public void sceneCaseGroupDayCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseJobGroupDayCount method in the Workspace service")
    public void caseJobGroupDayCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService.caseJobGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupDayCount Workspace")
    public void caseJobGroupDayCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.caseJobGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupDayCount Workspace")
    public void caseJobGroupDayCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.caseJobGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneCaseJobGroupDayCount method in the Workspace service")
    public void sceneCaseJobGroupDayCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = workspaceStatisticsService
            .sceneCaseJobGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupDayCount Workspace")
    public void sceneCaseJobGroupDayCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseJobGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupDayCount Workspace")
    public void sceneCaseJobGroupDayCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseJobGroupDayCount(ID, MOCK_DAY))
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
        List<CaseCountUserStatisticsResponse> responses = workspaceStatisticsService.caseGroupUserCount(MOCK_DAY, ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while caseGroupUserCount Workspace")
    public void caseGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.caseGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseGroupUserCount Workspace")
    public void caseGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.caseGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneCaseGroupUserCount method in the Workspace service")
    public void sceneCaseGroupUserCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().build());
        when(commonStatisticsRepository.getGroupUserCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountUserStatisticsResponse> responses = workspaceStatisticsService
            .sceneCaseGroupUserCount(MOCK_DAY, ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseGroupUserCount Workspace")
    public void sceneCaseGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseGroupUserCount Workspace")
    public void sceneCaseGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseGroupUserCount(MOCK_DAY, ID))
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
            .caseJobGroupUserCount(MOCK_DAY, ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupUserCount Workspace")
    public void caseJobGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.caseJobGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupUserCount Workspace")
    public void caseJobGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.caseJobGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneCaseJobGroupUserCount method in the Workspace service")
    public void sceneCaseJobGroupUserCount_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        List<CaseCountUserStatisticsResponse> caseCountStatisticsResponses =
            Lists.newArrayList(CaseCountUserStatisticsResponse.builder().build());
        when(commonStatisticsRepository.getGroupUserCountByJob(any(), any(), eq(SceneCaseJobEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountUserStatisticsResponse> responses = workspaceStatisticsService
            .sceneCaseJobGroupUserCount(MOCK_DAY, ID);
        assertThat(responses).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void sceneCaseJobGroupUserCount_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseJobGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void sceneCaseJobGroupUserCount_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.sceneCaseJobGroupUserCount(MOCK_DAY, ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the projectCasePercentage method in the Workspace service")
    public void projectCasePercentage_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(projectStatisticsService.caseCount(any())).thenReturn(MOCK_COUNT);
        when(projectStatisticsService.apiAllCount(any())).thenReturn(MOCK_COUNT);
        List<WorkspaceProjectCaseStatisticsResponse> dto = workspaceStatisticsService.projectCasePercentage(ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectCasePercentage_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.projectCasePercentage(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectCasePercentage_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.projectCasePercentage(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the projectSceneCasePercentage method in the Workspace service")
    public void projectSceneCasePercentage_test() {
        List<ProjectResponse> projectResponses = Lists.newArrayList(ProjectResponse.builder().id(ID).build());
        when(projectService.list(any())).thenReturn(projectResponses);
        when(projectStatisticsService.sceneCount(any())).thenReturn(MOCK_COUNT);
        when(projectStatisticsService.apiAllCount(any())).thenReturn(MOCK_COUNT);
        List<WorkspaceProjectCaseStatisticsResponse> dto = workspaceStatisticsService.projectCasePercentage(ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectSceneCasePercentage_smsException_test() {
        when(projectService.list(any()))
            .thenThrow(new ApiTestPlatformException(GET_WORKSPACE_PROJECT_SCENE_CASE_PERCENTAGE_ERROR));
        assertThatThrownBy(() -> workspaceStatisticsService.projectCasePercentage(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupUserCount Workspace")
    public void projectSceneCasePercentage_exception_test() {
        when(projectService.list(any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> workspaceStatisticsService.projectCasePercentage(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
