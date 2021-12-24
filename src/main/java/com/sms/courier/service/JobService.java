package com.sms.courier.service;

import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobReport;

public interface JobService {

    void handleJobReport(JobReport jobReport);

    void dispatcherJob(JobEntity jobEntity);

    void onError(JobEntity jobEntity, boolean resend);
}
