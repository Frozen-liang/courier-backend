package com.sms.courier.engine.service;

import com.sms.courier.dto.response.ApiTestCaseJobReportResponse;
import com.sms.courier.dto.response.ApiTestCaseJobResponse;
import com.sms.courier.dto.response.SceneCaseJobReportResponse;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import org.springframework.stereotype.Service;

@Service
public interface CaseDispatcherService {

    String dispatch(ApiTestCaseJobResponse caseJob);

    String dispatch(SceneCaseJobResponse caseJob);

    void sendJobReport(String userId, ApiTestCaseJobReportResponse caseReport);

    void sendJobReport(String userId, SceneCaseJobReportResponse caseReport);

    void sendCaseErrorMessage(String userId, String message);

    void sendSceneCaseErrorMessage(String userId, String message);
}
