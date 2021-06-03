package com.sms.satp.engine.service;

import com.sms.satp.engine.model.CaseJob;
import com.sms.satp.entity.job.ApiTestCaseJob;
import org.springframework.stereotype.Service;

@Service
public interface CaseDispatcherService {

    void dispatch(ApiTestCaseJob caseJob);

    void dispatch(CaseJob caseJob);

    void sendJobReport(String destination, ApiTestCaseJob caseJob);

    void sendJobReport(String destination, CaseJob caseJob);
}
