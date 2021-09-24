package com.sms.courier.service;

import com.sms.courier.entity.job.common.JobEntity;
import com.sms.courier.entity.job.common.JobReport;
import com.sms.courier.entity.job.common.RunningJobAck;
import java.util.List;

public interface JobService {

    void handleJobReport(JobReport jobReport);

    void reallocateJob(List<String> engineIds);

    void runningJobAck(RunningJobAck runningJobAck);

    void dispatcherJob(JobEntity jobEntity);
}
