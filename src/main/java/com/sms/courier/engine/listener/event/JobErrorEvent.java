package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.JobType;
import io.grpc.Status.Code;
import lombok.Getter;

@Getter
public class JobErrorEvent {

    private final String jobId;
    private final JobType jobType;
    private final Code code;

    public JobErrorEvent(String jobId, JobType jobType, Code code) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.code = code;
    }
}
