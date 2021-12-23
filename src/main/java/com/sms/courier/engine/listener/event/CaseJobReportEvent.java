package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.JobType;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobReport;
import lombok.Getter;

@Getter
public class CaseJobReportEvent {

    private final JobType jobType;
    private final GrpcCaseJobReport jobReport;

    public CaseJobReportEvent(JobType jobType, GrpcCaseJobReport jobReport) {
        this.jobType = jobType;
        this.jobReport = jobReport;
    }
}
