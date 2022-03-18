package com.sms.courier.repository;

import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface CommonStatisticsRepository {

    <T> List<CountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);

    <T> List<CountStatisticsResponse> getGroupDayCount(LocalDateTime dateTime, Class<T> entityClass);

    <T> List<CaseCountUserStatisticsResponse> getGroupUserCount(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);

    <T> List<CaseCountUserStatisticsResponse> getGroupUserCountByJob(List<String> projectIds, LocalDateTime dateTime,
        Class<T> entityClass);
}
