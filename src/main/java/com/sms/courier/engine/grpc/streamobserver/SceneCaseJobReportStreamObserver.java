package com.sms.courier.engine.grpc.streamobserver;


import com.sms.courier.common.enums.JobType;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobReport;
import com.sms.courier.engine.listener.event.JobCompletedEvent;
import com.sms.courier.engine.listener.event.JobErrorEvent;
import com.sms.courier.engine.listener.event.SceneCaseJobReportEvent;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class SceneCaseJobReportStreamObserver implements StreamObserver<GrpcSceneCaseJobReport> {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final String jobId;
    private final JobType jobType;

    public SceneCaseJobReportStreamObserver(String jobId,
        JobType jobType, ApplicationEventPublisher applicationEventPublisher) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onNext(GrpcSceneCaseJobReport jobReport) {
        log.info("Receive scene case job report :{}", jobReport);
        applicationEventPublisher.publishEvent(new SceneCaseJobReportEvent(jobType, jobReport));
        applicationEventPublisher.publishEvent(new JobCompletedEvent(jobId, jobType));
    }

    @Override
    public void onError(Throwable t) {
        Status status = Status.fromThrowable(t);
        log.error("SceneCaseJob error! jobId : {} jobType : {}", jobId, jobType, t);
        applicationEventPublisher.publishEvent(new JobErrorEvent(jobId, jobType, status.getCode()));
    }

    @Override
    public void onCompleted() {
        log.info("Completed! jobId : {} jobType : {}", jobId, jobType);
    }
}
