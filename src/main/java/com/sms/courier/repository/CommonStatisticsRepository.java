package com.sms.courier.repository;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface CommonStatisticsRepository {

    <T> List<CaseCountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);

    <T> List<CaseCountUserStatisticsResponse> getGroupUserCount(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);

    <T> List<CaseCountUserStatisticsResponse> getGroupUserCountByJob(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);
}
