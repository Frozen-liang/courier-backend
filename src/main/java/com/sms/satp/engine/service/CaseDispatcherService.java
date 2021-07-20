package com.sms.satp.engine.service;

import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import com.sms.satp.entity.job.SceneCaseJobEntity;
import com.sms.satp.entity.job.common.CaseReport;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CaseDispatcherService {

    void dispatch(ApiTestCaseJobEntity caseJob);

    void dispatch(SceneCaseJobEntity caseJob);

    void sendJobReport(String userId, CaseReport caseReport);

    void sendJobReport(String userId, List<CaseReport> caseReport);

    void sendErrorMessage(String userId, String message);
}
