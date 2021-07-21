package com.sms.satp.repository;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import org.springframework.data.domain.Page;

public interface CustomizedApiTestCaseJobRepository {

    Page<ApiTestCaseJobEntity> page(ApiTestCaseJobPageRequest apiTestCaseJobPageRequest);

    ApiTestCaseJobEntity findRecentlyCaseReportByCaseId(String apiTestCaseId);
}
