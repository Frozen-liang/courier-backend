package com.sms.satp.engine.service;

import com.sms.satp.dto.response.ApiTestCaseJobReportResponse;
import com.sms.satp.dto.response.ApiTestCaseJobResponse;
import com.sms.satp.dto.response.SceneCaseJobReportResponse;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import org.springframework.stereotype.Service;

@Service
public interface CaseDispatcherService {

    String dispatch(ApiTestCaseJobResponse caseJob);

    String dispatch(SceneCaseJobResponse caseJob);

    void sendJobReport(String userId, ApiTestCaseJobReportResponse caseReport);

    void sendJobReport(String userId, SceneCaseJobReportResponse caseReport);

    void sendErrorMessage(String userId, String message);
}
