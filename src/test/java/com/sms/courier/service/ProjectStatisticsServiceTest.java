package com.sms.courier.service;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.service.impl.ProjectStatisticsServiceImpl;
import java.util.List;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for ProjectStatisticsService")
public class ProjectStatisticsServiceTest {

    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final ApiService apiService = mock(ApiService.class);
    private final SceneCaseService sceneCaseService = mock(SceneCaseService.class);
    private final ApiTestCaseService apiTestCaseService = mock(ApiTestCaseService.class);
    private final CommonStatisticsRepository commonStatisticsRepository = mock(CommonStatisticsRepository.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        mock(CustomizedSceneCaseJobRepository.class);
    private final ProjectStatisticsService projectStatisticsService =
        new ProjectStatisticsServiceImpl(customizedApiRepository, apiService, sceneCaseService, apiTestCaseService,
            commonStatisticsRepository, customizedSceneCaseJobRepository);

    private static final String ID = ObjectId.get().toString();
    private static final Integer MOCK_DAY = 7;

    @Test
    @DisplayName("Test for sceneCountPage in ProjectStatisticsService")
    public void sceneCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(customizedApiRepository.sceneCountPage(any())).thenReturn(page);
        Page<ApiPageResponse> dtoPage = projectStatisticsService
            .sceneCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for sceneCountPage in ProjectStatisticsService")
    public void sceneCountPage_exception_test() {
        when(customizedApiRepository.sceneCountPage(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneCountPage(ApiIncludeCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for caseCountPage in ProjectStatisticsService")
    public void caseCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(customizedApiRepository.caseCountPage(any())).thenReturn(page);
        Page<ApiPageResponse> dtoPage = projectStatisticsService.caseCountPage(ApiIncludeCaseRequest.builder().build());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for caseCountPage in ProjectStatisticsService")
    public void caseCountPage_exception_test() {
        when(customizedApiRepository.caseCountPage(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseCountPage(ApiIncludeCaseRequest.builder().build()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseGroupDayCount method in the ProjectStatisticsService")
    public void caseGroupDayCount_test() {
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = projectStatisticsService.caseGroupDayCount(ID);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }


    @Test
    @DisplayName("An exception occurred while caseGroupDayCount ProjectStatisticsService")
    public void caseGroupDayCount_exception_test() {
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseGroupDayCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for sceneCount in ProjectStatisticsService")
    public void sceneCount_test() {
        when(customizedApiRepository.sceneCount(any())).thenReturn(1L);
        Long count = projectStatisticsService.sceneCount(new ObjectId());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test sceneCount in ProjectStatisticsService.")
    public void sceneCount_exception_test() {
        when(customizedApiRepository.sceneCount(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneCount(new ObjectId()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for caseCount in ProjectStatisticsService")
    public void caseCount_test() {
        when(customizedApiRepository.caseCount(any())).thenReturn(1L);
        Long count = projectStatisticsService.caseCount(new ObjectId());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test caseCount in ProjectStatisticsService.")
    public void caseCount_exception_test() {
        when(customizedApiRepository.caseCount(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseCount(new ObjectId()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for apiAllCount in ProjectStatisticsService")
    public void apiAllCount_test() {
        when(apiService.count(any())).thenReturn(1L);
        Long count = projectStatisticsService.apiAllCount(ID);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test apiAllCount in ProjectStatisticsService.")
    public void apiAllCount_exception_test() {
        when((apiService.count(any()))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.apiAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for sceneAllCount in ProjectStatisticsService")
    public void sceneAllCount_test() {
        when(sceneCaseService.count(any())).thenReturn(1L);
        Long count = projectStatisticsService.sceneAllCount(ID);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test sceneAllCount in ProjectStatisticsService.")
    public void sceneAllCount_exception_test() {
        when((sceneCaseService.count(any()))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for caseAllCount in ProjectStatisticsService")
    public void caseAllCount_test() {
        when(apiTestCaseService.count(any())).thenReturn(1L);
        Long count = projectStatisticsService.caseAllCount(ID);
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test caseAllCount in ProjectStatisticsService.")
    public void caseAllCount_exception_test() {
        when((apiTestCaseService.count(any()))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseAllCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for sceneCaseGroupDayCount in ProjectStatisticsService")
    public void sceneCaseGroupDayCount_test() {
        List<CaseCountStatisticsResponse> responses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenReturn(responses);
        List<CaseCountStatisticsResponse> dto = projectStatisticsService.sceneCaseGroupDayCount(ID);
        assertThat(dto).isNotEmpty();
    }

    @Test
    @DisplayName("An exception occurred while test sceneCaseGroupDayCount in ProjectStatisticsService")
    public void sceneCaseGroupDayCount_exception_test() {
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(SceneCaseEntity.class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneCaseGroupDayCount(ID))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseJobGroupDayCount method in the ProjectStatisticsService")
    public void caseJobGroupDayCount_test() {
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseJobEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = projectStatisticsService.caseJobGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while caseJobGroupDayCount ProjectStatisticsService")
    public void caseJobGroupDayCount_exception_test() {
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseJobEntity.class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.caseJobGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the sceneCaseJobGroupDayCount method in the ProjectStatisticsService")
    public void sceneCaseJobGroupDayCount_test() {
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(customizedSceneCaseJobRepository.getGroupDayCount(any(), any()))
            .thenReturn(caseCountStatisticsResponses);
        List<CaseCountStatisticsResponse> responses = projectStatisticsService.sceneCaseJobGroupDayCount(ID, MOCK_DAY);
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }

    @Test
    @DisplayName("An exception occurred while sceneCaseJobGroupDayCount ProjectStatisticsService")
    public void sceneCaseJobGroupDayCount_exception_test() {
        when(customizedSceneCaseJobRepository.getGroupDayCount(any(), any()))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.sceneCaseJobGroupDayCount(ID, MOCK_DAY))
            .isInstanceOf(ApiTestPlatformException.class);
    }

}
