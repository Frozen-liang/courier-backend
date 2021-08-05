package com.sms.courier.repository;

import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseJobRepository {

    Page<SceneCaseJobEntity> page(SceneCaseJobRequest sceneCaseJobRequest);
}
