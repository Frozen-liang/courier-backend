package com.sms.courier.service.impl;

import com.google.common.collect.Lists;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractStatisticsService {

    public List<CaseCountStatisticsResponse> handleResponses(
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
