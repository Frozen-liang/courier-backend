package com.sms.courier.repository;

import com.sms.courier.dto.response.CountStatisticsResponse;
import java.time.LocalDateTime;
import java.util.List;

public interface CustomizedSceneCaseJobRepository {

    List<CountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime);
}
