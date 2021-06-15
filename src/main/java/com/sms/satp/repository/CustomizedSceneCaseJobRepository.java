package com.sms.satp.repository;

import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.entity.job.SceneCaseJob;
import org.springframework.data.domain.Page;

public interface CustomizedSceneCaseJobRepository {

    Page<SceneCaseJob> page(SceneCaseJobRequest sceneCaseJobRequest);
}
