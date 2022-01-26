package com.sms.courier.engine.impl;

import static com.sms.courier.common.field.EngineMemberField.CASE_TASK;
import static com.sms.courier.common.field.EngineMemberField.SCENE_CASE_TASK;
import static com.sms.courier.engine.grpc.loadbalancer.Constants.AUTHORIZATION;
import static com.sms.courier.engine.grpc.loadbalancer.Constants.JOB_ID;
import static com.sms.courier.engine.grpc.loadbalancer.Constants.JOT_TYPE;
import static com.sms.courier.engine.grpc.loadbalancer.Constants.TASK_TYPE;

import com.sms.courier.common.enums.JobType;
import com.sms.courier.engine.EngineJobManagement;
import com.sms.courier.engine.grpc.api.v1.EngineGrpc.EngineStub;
import com.sms.courier.engine.grpc.streamobserver.CaseJobReportStreamObserver;
import com.sms.courier.engine.grpc.streamobserver.SceneCaseJobReportStreamObserver;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.utils.AesUtil;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EngineJobManagementImpl implements EngineJobManagement {

    private final ApplicationContext applicationContext;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GrpcMapper grpcMapper;

    public EngineJobManagementImpl(ApplicationContext applicationContext,
        ApplicationEventPublisher applicationEventPublisher,
        GrpcMapper grpcMapper) {
        this.applicationContext = applicationContext;
        this.applicationEventPublisher = applicationEventPublisher;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void dispatcherJob(SceneCaseJobEntity request) {
        EngineStub engineStub = applicationContext.getBean(EngineStub.class);
        ClientInterceptor clientInterceptor = getClientInterceptor(request.getId(), JobType.SCENE_CASE);
        engineStub.withOption(TASK_TYPE, SCENE_CASE_TASK.getName())
            .withInterceptors(clientInterceptor)
            .sendSceneCaseJob(grpcMapper.toGrpcJobRequest(request),
                new SceneCaseJobReportStreamObserver(request.getId(), JobType.SCENE_CASE, applicationEventPublisher));
    }

    @Override
    public void dispatcherJob(ScheduleSceneCaseJobEntity request) {
        EngineStub engineStub = applicationContext.getBean(EngineStub.class);
        ClientInterceptor clientInterceptor = getClientInterceptor(request.getId(), JobType.SCHEDULER_SCENE_CASE);
        engineStub.withOption(TASK_TYPE, SCENE_CASE_TASK.getName())
            .withInterceptors(clientInterceptor)
            .sendSceneCaseJob(grpcMapper.toGrpcJobRequest(request),
                new SceneCaseJobReportStreamObserver(request.getId(), JobType.SCHEDULER_SCENE_CASE,
                    applicationEventPublisher));
    }

    @Override
    public void dispatcherJob(ApiTestCaseJobEntity request) {
        EngineStub engineStub = applicationContext.getBean(EngineStub.class);
        ClientInterceptor clientInterceptor = getClientInterceptor(request.getId(), JobType.CASE);
        engineStub.withOption(TASK_TYPE, CASE_TASK.getName())
            .withInterceptors(clientInterceptor)
            .sendCaseJob(grpcMapper.toGrpcJobRequest(request), new CaseJobReportStreamObserver(request.getId(),
                JobType.CASE, applicationEventPublisher));
    }

    @Override
    public void dispatcherJob(ScheduleCaseJobEntity request) {
        EngineStub engineStub = applicationContext.getBean(EngineStub.class);
        ClientInterceptor clientInterceptor = getClientInterceptor(request.getId(), JobType.SCHEDULE_CATE);
        engineStub.withOption(TASK_TYPE, CASE_TASK.getName())
            .withInterceptors(clientInterceptor)
            .sendCaseJob(grpcMapper.toGrpcJobRequest(request),
                new CaseJobReportStreamObserver(request.getId(), JobType.SCHEDULE_CATE,
                    applicationEventPublisher));
    }


    private ClientInterceptor getClientInterceptor(String jobId, JobType jobType) {
        Metadata metadata = new Metadata();
        metadata.put(JOB_ID, jobId);
        metadata.put(JOT_TYPE, jobType.name());
        metadata.put(AUTHORIZATION, AesUtil.encrypt(RandomStringUtils.random(8, true, true)));
        return MetadataUtils.newAttachHeadersInterceptor(metadata);
    }
}
