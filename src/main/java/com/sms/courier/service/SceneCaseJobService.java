package com.sms.courier.service;

import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.security.pojo.CustomUser;
import java.util.List;
import org.springframework.data.domain.Page;

public interface SceneCaseJobService {

    Page<SceneCaseJobResponse> page(SceneCaseJobRequest sceneCaseJobRequest);

    SceneCaseJobResponse get(String jobId);

    void handleJobReport(SceneCaseJobReport jobReport);

    void runJob(AddSceneCaseJobRequest addSceneCaseJobRequest, CustomUser customUser);

    void reallocateJob(List<String> engineIds);

    List<SceneCaseJobResponse> buildJob(AddSceneCaseJobRequest sceneCaseJobRequest);

    Boolean editReport(SceneCaseJobReport sceneCaseJobReport);
}
