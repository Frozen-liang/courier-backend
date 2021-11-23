package com.sms.courier.service;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.impl.WorkspaceStatisticsServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WorkspaceStatisticsServiceTest {

    private final ProjectService projectService = mock(ProjectService.class);
    private final CommonStatisticsRepository commonStatisticsRepository = mock(CommonStatisticsRepository.class);
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository = mock(
        CustomizedSceneCaseRepository.class);
    private final WorkspaceStatisticsService workspaceStatisticsService =
        new WorkspaceStatisticsServiceImpl(projectService, commonStatisticsRepository, customizedSceneCaseRepository);
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

}
