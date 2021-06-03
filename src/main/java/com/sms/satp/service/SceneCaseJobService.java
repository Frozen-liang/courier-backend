package com.sms.satp.service;

import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.entity.job.SceneCaseJob;
import java.util.List;
import org.springframework.data.domain.Page;

public interface SceneCaseJobService {

    Boolean add(AddSceneCaseJobRequest request);

    Page<SceneCaseJob> page(List<String> userIds, PageDto pageDto);

    SceneCaseJob get(String jobId);

    Boolean edit(SceneCaseJob sceneCaseJob);
}
