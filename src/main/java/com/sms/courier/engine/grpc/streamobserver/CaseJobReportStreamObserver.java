package com.sms.courier.engine.grpc.streamobserver;


import com.sms.courier.common.enums.JobType;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobReport;
import com.sms.courier.engine.listener.event.CaseJobReportEvent;
import com.sms.courier.engine.listener.event.JobCompletedEvent;
import com.sms.courier.engine.listener.event.JobErrorEvent;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class CaseJobReportStreamObserver implements StreamObserver<GrpcCaseJobReport> {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String jobId;
    private final JobType jobType;

    public CaseJobReportStreamObserver(String jobId,
        JobType jobType, ApplicationEventPublisher applicationEventPublisher) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onNext(GrpcCaseJobReport jobReport) {
        log.info("Receive case job report!");
        JobType jobType = JobType.valueOf(jobReport.getJobType());
        applicationEventPublisher.publishEvent(new CaseJobReportEvent(jobType, jobReport));
        applicationEventPublisher.publishEvent(new JobCompletedEvent(jobReport.getJobId(), jobType));
    }

    @Override
    public void onError(Throwable t) {
        Status status = Status.fromThrowable(t);
        log.error("CaseJob error! jobId : {} jobType : {}", jobId, jobType, t);
        applicationEventPublisher.publishEvent(new JobErrorEvent(jobId, jobType, status.getCode()));
    }

    @Override
    public void onCompleted() {
        log.info("Completed!");
    }
}
