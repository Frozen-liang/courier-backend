package com.sms.satp.service;

import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import com.sms.satp.entity.job.SceneCaseJobReport;
import com.sms.satp.security.pojo.CustomUser;
import org.springframework.data.domain.Page;

public interface SceneCaseJobService {

    Page<SceneCaseJobResponse> page(SceneCaseJobRequest sceneCaseJobRequest);

    SceneCaseJobResponse get(String jobId);

    void handleJobReport(SceneCaseJobReport jobReport);

    void runJob(AddSceneCaseJobRequest addSceneCaseJobRequest, CustomUser customUser);

    void deleteById(String id);
}
