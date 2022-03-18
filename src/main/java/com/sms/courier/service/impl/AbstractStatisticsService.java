package com.sms.courier.service.impl;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractStatisticsService {

    private final CommonStatisticsRepository commonStatisticsRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final CustomizedApiRepository customizedApiRepository;
    protected final Map<String, Function<List<String>, Long>> allCountTypeMap = new HashMap<>();
    protected final Map<String, Class<?>> groupQueryTypeMap = new HashMap<>();

    protected AbstractStatisticsService(CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedApiRepository customizedApiRepository) {
        this.commonStatisticsRepository = commonStatisticsRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.customizedApiRepository = customizedApiRepository;
    }

    @PostConstruct
    public void allCountTypeMapInit() {
        allCountTypeMap.put(StatisticsCountType.API.getName(), customizedApiRepository::count);
        allCountTypeMap.put(StatisticsCountType.API_TEST_CASE.getName(), customizedApiTestCaseRepository::count);
        allCountTypeMap.put(StatisticsCountType.SCENE_CASE.getName(), customizedSceneCaseRepository::count);

        groupQueryTypeMap.put(StatisticsGroupQueryType.API_TEST_CASE.getName(), ApiTestCaseEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.SCENE_CASE.getName(), SceneCaseEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.API_TEST_CASE_JOB.getName(), ApiTestCaseJobEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.SCENE_CASE_JOB.getName(), SceneCaseJobEntity.class);
    }

    protected <T> List<CaseCountStatisticsResponse> groupDay(List<String> projectIds, Integer day,
        Class<T> entityClass) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(day);
        List<CaseCountStatisticsResponse> responses = CollectionUtils.isNotEmpty(projectIds)
            ? commonStatisticsRepository.getGroupDayCount(projectIds, dateTime, entityClass)
            : Lists.newArrayList();
        return handleResponses(responses, day);
    }

    protected <T> List<CaseCountUserStatisticsResponse> groupUser(List<String> projectIds, Integer day,
        Class<T> entityClass) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(day);
        return CollectionUtils.isNotEmpty(projectIds)
            ? commonStatisticsRepository.getGroupUserCount(projectIds, dateTime, entityClass)
            : Lists.newArrayList();
    }

    protected <T> List<CaseCountUserStatisticsResponse> groupUserByJob(List<String> projectIds, Integer day,
        Class<T> entityClass) {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(day);
        return CollectionUtils.isNotEmpty(projectIds)
            ? commonStatisticsRepository.getGroupUserCountByJob(projectIds, dateTime, entityClass)
            : Lists.newArrayList();
    }

    protected List<CaseCountStatisticsResponse> handleResponses(
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses, Integer day) {
        List<CaseCountStatisticsResponse> responses = Lists.newArrayList(caseCountStatisticsResponses);
        List<LocalDate> localDateList = responses.stream().map(CaseCountStatisticsResponse::getDay)
            .collect(Collectors.toList());
        responses.addAll(IntStream.range(0, day)
            .mapToObj(i -> LocalDate.now().minusDays(i))
            .filter(i -> !localDateList.contains(i))
            .map(data -> CaseCountStatisticsResponse.builder().day(data).count(0).build())
            .collect(Collectors.toList()));
        responses.sort(Comparator.comparing(CaseCountStatisticsResponse::getDay));
        return responses;
    }

}
