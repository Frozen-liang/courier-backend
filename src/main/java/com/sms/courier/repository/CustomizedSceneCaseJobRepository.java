package com.sms.courier.repository;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedSceneCaseJobRepository {

    List<CaseCountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime);
}
