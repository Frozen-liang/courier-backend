package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.JobType;
import lombok.Getter;

@Getter
public class JobCompletedEvent {

    private final String jobId;
    private final JobType jobType;

    public JobCompletedEvent(String jobId, JobType jobType) {
        this.jobId = jobId;
        this.jobType = jobType;
    }
}
