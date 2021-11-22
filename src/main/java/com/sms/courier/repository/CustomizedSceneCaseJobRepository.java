package com.sms.courier.repository;

import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseJobRepository {

    Page<SceneCaseJobEntity> page(SceneCaseJobRequest sceneCaseJobRequest);

    List<CaseCountStatisticsResponse> getGroupDayCount(List<String> projectIds, LocalDateTime dateTime);
}
