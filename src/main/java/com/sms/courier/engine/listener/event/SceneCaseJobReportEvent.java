package com.sms.courier.engine.listener.event;

import com.sms.courier.common.enums.JobType;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobReport;
import lombok.Getter;

@Getter
public class SceneCaseJobReportEvent {

    private final JobType jobType;
    private final GrpcSceneCaseJobReport jobReport;

    public SceneCaseJobReportEvent(JobType jobType, GrpcSceneCaseJobReport jobReport) {
        this.jobType = jobType;
        this.jobReport = jobReport;
    }
}
