package com.sms.courier.common.listener.event;

import com.sms.courier.common.enums.JobStatus;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ScheduleJobRecordEvent {

    private final String id;
    private final String jobId;
    private final String caseId;
    private final JobStatus jobStatus;

    private ScheduleJobRecordEvent(String id, String jobId, String caseId, JobStatus jobStatus) {
        this.id = id;
        this.jobId = jobId;
        this.caseId = caseId;
        this.jobStatus = jobStatus;
    }

    public static ScheduleJobRecordEvent create(String id, String jobId, String caseId, JobStatus jobStatus) {
        Objects.requireNonNull(id, "The id must not be null.");
        Objects.requireNonNull(jobId, "The jobId must not be null.");
        Objects.requireNonNull(caseId, "The caseId must not be null.");
        Objects.requireNonNull(jobStatus, "The jobStatus must not be null.");
        return new ScheduleJobRecordEvent(id, jobId, caseId, jobStatus);
    }
}
