package com.sms.courier.repository;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.dto.request.ApiTestCaseJobPageRequest;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import org.springframework.data.domain.Page;

public interface CustomizedApiTestCaseJobRepository {

    Page<ApiTestCaseJobEntity> page(ApiTestCaseJobPageRequest apiTestCaseJobPageRequest);

    void updateJobById(String id, String engineId, JobStatus jobStatus);
}
