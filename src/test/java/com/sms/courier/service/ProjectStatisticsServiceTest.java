package com.sms.courier.service;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.impl.ProjectStatisticsServiceImpl;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.util.ReflectionUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tests for ProjectStatisticsService")
public class ProjectStatisticsServiceTest {

    private final CustomizedApiRepository customizedApiRepository = mock(CustomizedApiRepository.class);
    private final CommonStatisticsRepository commonStatisticsRepository = mock(CommonStatisticsRepository.class);
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository =
        mock(CustomizedSceneCaseJobRepository.class);
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository =
        mock(CustomizedApiTestCaseRepository.class);
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository = mock(
        CustomizedSceneCaseRepository.class);
    private final ProjectStatisticsService projectStatisticsService =
        new ProjectStatisticsServiceImpl(customizedApiRepository,
            commonStatisticsRepository, customizedSceneCaseJobRepository,
            customizedApiTestCaseRepository, customizedSceneCaseRepository);

    private static final String ID = ObjectId.get().toString();
    private static final Integer MOCK_DAY = 7;


    @Test
    @DisplayName("Test for caseCountPage in ProjectStatisticsService")
    public void caseCountPage_test() {
        Page<ApiPageResponse> page = mock(Page.class);
        when(page.getContent()).thenReturn(Lists.newArrayList(ApiPageResponse.builder().id(ID).build()));
        when(customizedApiRepository.caseCountPage(any())).thenReturn(page);
        Page<ApiPageResponse> dtoPage =
            projectStatisticsService
                .caseCountPage(ApiIncludeCaseRequest.builder().build(), StatisticsCountType.API_TEST_CASE.getName());
        assertThat(dtoPage.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Test for caseCountPage in ProjectStatisticsService")
    public void caseCountPage_exception_test() {
        when(customizedApiRepository.caseCountPage(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService
            .caseCountPage(ApiIncludeCaseRequest.builder().build(), StatisticsCountType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test the caseGroupDayCount method in the ProjectStatisticsService")
    public void caseGroupDayCount_test() {
        List<CountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenReturn(caseCountStatisticsResponses);
        List<CountStatisticsResponse> responses = projectStatisticsService.groupDayCount(ID,
            Constants.CASE_DAY, StatisticsGroupQueryType.API_TEST_CASE.getName());
        assertThat(responses.size()).isEqualTo(Constants.CASE_DAY);
    }


    @Test
    @DisplayName("An exception occurred while caseGroupDayCount ProjectStatisticsService")
    public void caseGroupDayCount_exception_test() {
        ProjectStatisticsService statisticsService = mock(ProjectStatisticsServiceImpl.class,
            Mockito.CALLS_REAL_METHODS);
        Map<String, Class<?>> groupQueryTypeMap = new HashMap<>();
        groupQueryTypeMap.put(StatisticsGroupQueryType.API_TEST_CASE.getName(), ApiTestCaseEntity.class);
        Field field = ReflectionUtils.findField(statisticsService.getClass().getSuperclass(),
            "groupQueryTypeMap");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field,statisticsService,groupQueryTypeMap);

        when(commonStatisticsRepository.getGroupDayCount(any(), any(), eq(ApiTestCaseEntity.class)))
            .thenThrow(new RuntimeException());
        assertThatThrownBy(() -> statisticsService.groupDayCount(ID,
            Constants.CASE_DAY, StatisticsGroupQueryType.API_TEST_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }

    @Test
    @DisplayName("Test for sceneCount in ProjectStatisticsService")
    public void sceneCount_test() {
        when(customizedApiRepository.sceneCount(any())).thenReturn(1L);
        Long count = projectStatisticsService.caseCount(new ObjectId(), StatisticsCountType.SCENE_CASE.getName());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test sceneCount in ProjectStatisticsService.")
    public void sceneCount_exception_test() {
        when(customizedApiRepository.sceneCount(any())).thenThrow(new RuntimeException());
        assertThatThrownBy(
            () -> projectStatisticsService.caseCount(new ObjectId(), StatisticsCountType.SCENE_CASE.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }


    @Test
    @DisplayName("Test for apiAllCount in ProjectStatisticsService")
    public void apiAllCount_test() throws NoSuchFieldException {
        ProjectStatisticsService statisticsService = mock(ProjectStatisticsServiceImpl.class,
            Mockito.CALLS_REAL_METHODS);
        Map<String, Function<List<String>, Long>> allCountTypeMap = new HashMap<>();
        allCountTypeMap.put(StatisticsCountType.API.getName(), customizedApiRepository::count);
        Field field = ReflectionUtils.findField(statisticsService.getClass().getSuperclass(),
            "allCountTypeMap");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field,statisticsService,allCountTypeMap);

        when(customizedApiRepository.count(any())).thenReturn(1L);
        Long count = statisticsService.allCount(ID, StatisticsCountType.API.getName());
        assertThat(count).isEqualTo(1L);
    }

    @Test
    @DisplayName("An exception occurred while test apiAllCount in ProjectStatisticsService.")
    public void apiAllCount_exception_test() {
        when((customizedApiRepository.count(any()))).thenThrow(new RuntimeException());
        assertThatThrownBy(() -> projectStatisticsService.allCount(ID, StatisticsCountType.API.getName()))
            .isInstanceOf(ApiTestPlatformException.class);
    }


    @Test
    @DisplayName("Test the sceneCaseJobGroupDayCount method in the ProjectStatisticsService")
    public void sceneCaseJobGroupDayCount_test() {
        List<CountStatisticsResponse> caseCountStatisticsResponses = Lists.newArrayList();
        when(customizedSceneCaseJobRepository.getGroupDayCount(any(), any()))
            .thenReturn(caseCountStatisticsResponses);
        List<CountStatisticsResponse> responses = projectStatisticsService.sceneCaseJobGroupDayCount(ID, MOCK_DAY);
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
