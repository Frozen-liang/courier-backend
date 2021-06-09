package com.sms.satp.repository;

import com.sms.satp.dto.request.ApiTestCaseJobPageRequest;
import com.sms.satp.entity.job.ApiTestCaseJob;
import org.springframework.data.domain.Page;

public interface CustomizedApiTestCaseJobRepository {

    Page<ApiTestCaseJob> page(ApiTestCaseJobPageRequest apiTestCaseJobPageRequest);

}
