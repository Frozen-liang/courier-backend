package com.sms.satp.repository;

import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.entity.job.SceneCaseJobEntity;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseJobRepository {

    Page<SceneCaseJobEntity> page(SceneCaseJobRequest sceneCaseJobRequest);
}
