package com.sms.courier.service.impl;

import com.google.common.collect.Lists;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.repository.CommonStatisticsRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections4.CollectionUtils;

public abstract class AbstractStatisticsService {

    private final CommonStatisticsRepository commonStatisticsRepository;

    protected AbstractStatisticsService(CommonStatisticsRepository commonStatisticsRepository) {
        this.commonStatisticsRepository = commonStatisticsRepository;
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
