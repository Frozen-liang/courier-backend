package com.sms.courier.engine.listener;

import static com.sms.courier.common.field.EngineMemberField.TASK_COUNT;
import static com.sms.courier.docker.enmu.ContainerField.CONTAINER_STATUS;

import com.sms.courier.common.enums.ContainerStatus;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.common.field.EngineMemberField;
import com.sms.courier.engine.EngineMemberManagement;
import com.sms.courier.engine.grpc.api.v1.GrpcCaseJobReport;
import com.sms.courier.engine.grpc.api.v1.GrpcFunctionResponse;
import com.sms.courier.engine.grpc.api.v1.GrpcFunctionResponse.Builder;
import com.sms.courier.engine.grpc.api.v1.GrpcSceneCaseJobReport;
import com.sms.courier.engine.grpc.server.EngineRegisterServiceImpl;
import com.sms.courier.engine.listener.event.CaseJobReportEvent;
import com.sms.courier.engine.listener.event.EngineGlobalFunctionEvent;
import com.sms.courier.engine.listener.event.EngineProjectFunctionEvent;
import com.sms.courier.engine.listener.event.EngineRegisterEvent;
import com.sms.courier.engine.listener.event.EngineStatusEvent;
import com.sms.courier.engine.listener.event.EngineTaskRecordEvent;
import com.sms.courier.engine.listener.event.JobCompletedEvent;
import com.sms.courier.engine.listener.event.JobErrorEvent;
import com.sms.courier.engine.listener.event.SceneCaseJobReportEvent;
import com.sms.courier.engine.model.EngineMemberEntity;
import com.sms.courier.entity.job.ApiTestCaseJobReport;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.mapper.GrpcMapper;
import com.sms.courier.repository.CommonRepository;
import com.sms.courier.service.JobServiceFactory;
import io.grpc.Status.Code;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EngineListener implements ApplicationContextAware {

    private EngineMemberManagement engineMemberManagement;
    private JobServiceFactory jobServiceFactory;
    private GrpcMapper grpcMapper;
    private CommonRepository commonRepository;
    private ApplicationContext applicationContext;

    @EventListener
    @Async("commonExecutor")
    public void engineRegister(EngineRegisterEvent event) {
        log.info("Engine : {} register!", event.getRequest());
        engineMemberManagement.registerEngine(event.getRequest());
    }

    @EventListener
    @Async("engineExecutor")
    public void jobReport(SceneCaseJobReportEvent event) {
        GrpcSceneCaseJobReport jobReport = event.getJobReport();
        SceneCaseJobReport sceneCaseJobReport = grpcMapper.toJobReport(jobReport);
        jobServiceFactory.getJobService(event.getJobType()).handleJobReport(sceneCaseJobReport);
    }

    @EventListener
    @Async("engineExecutor")
    public void jobReport(CaseJobReportEvent event) {
        GrpcCaseJobReport jobReport = event.getJobReport();
        ApiTestCaseJobReport caseJobReport = grpcMapper.toJobReport(jobReport);
        jobServiceFactory.getJobService(event.getJobType())
            .handleJobReport(caseJobReport);
    }

    @EventListener
    public void jobCompleted(JobCompletedEvent event) {
        log.info("Job completed jobType : {}", event.getJobType());
        commonRepository.findById(event.getJobId(), event.getJobType().getEntityClass()).ifPresent(job -> {
            String engineId = job.getEngineId();
            completedTaskRecord(engineId, event.getJobType().getTaskType(), Code.OK);
        });
    }

    @EventListener
    @Async("engineExecutor")
    public void jobError(JobErrorEvent event) {
        log.info("Job error jobType : {}", event.getJobType());
        commonRepository.findById(event.getJobId(), event.getJobType().getEntityClass()).ifPresent(job -> {
            String engineId = job.getEngineId();
            completedTaskRecord(engineId, event.getJobType().getTaskType(), event.getCode());
            boolean resend = !StringUtils.isBlank(engineId) && CollectionUtils
                .isNotEmpty(engineMemberManagement.getAvailableEngine());
            jobServiceFactory.getJobService(event.getJobType())
                .onError(job, resend);
        });
    }

    @EventListener
    @Async("commonExecutor")
    public void projectFunction(EngineProjectFunctionEvent event) {
        log.info("Push project function! {}", event.getFunctions());
        EngineRegisterServiceImpl engineRegisterService = applicationContext.getBean(EngineRegisterServiceImpl.class);
        Builder grpcFunctionBuilder = GrpcFunctionResponse.newBuilder()
            .setOperationType(event.getOperationType().getCode());
        event.getFunctions().forEach(function -> engineRegisterService
            .pushFunction(grpcFunctionBuilder.setFunction(grpcMapper.toGrpcFunction(function)).build()));
    }

    @EventListener
    @Async("commonExecutor")
    public void globalFunction(EngineGlobalFunctionEvent event) {
        log.info("Push global function! {}", event.getFunctions());
        EngineRegisterServiceImpl engineRegisterService = applicationContext.getBean(EngineRegisterServiceImpl.class);
        Builder grpcFunctionBuilder = GrpcFunctionResponse.newBuilder()
            .setOperationType(event.getOperationType().getCode());
        event.getFunctions().forEach(function -> engineRegisterService
            .pushFunction(grpcFunctionBuilder.setFunction(grpcMapper.toGrpcFunction(function)).build()));
    }

    @EventListener
    public void engineStatus(EngineStatusEvent event) {
        Query query = Query.query(Criteria.where(EngineMemberField.NAME.getName()).is(event.getName()));
        Update update = new Update();
        update.set(EngineMemberField.STATUS.getName(), event.getStatus());
        commonRepository.updateField(query, update, EngineMemberEntity.class);
    }

    @EventListener
    public void engineTaskRecord(EngineTaskRecordEvent event) {
        log.info("task record! {} {}", event.getName(), event.getTaskType());
        if (StringUtils.isNotBlank(event.getName())) {
            runningTaskRecord(event.getName(), event.getTaskType());
            updateJobEngineHost(event, event.getName());
        }
    }

    private void updateJobEngineHost(EngineTaskRecordEvent event, String name) {
        Query query = Query.query(Criteria.where(CommonField.ID.getName()).is(event.getJobId()));
        Update update = new Update();
        update.set("engineId", name);
        commonRepository.updateField(query, update, JobType.valueOf(event.getJobType()).getEntityClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.commonRepository = applicationContext.getBean(CommonRepository.class);
        this.grpcMapper = applicationContext.getBean(GrpcMapper.class);
        this.jobServiceFactory = applicationContext.getBean(JobServiceFactory.class);
        this.engineMemberManagement = applicationContext.getBean(EngineMemberManagement.class);
        this.applicationContext = applicationContext;
    }

    private void runningTaskRecord(String name, String taskType) {
        Query query = Query.query(Criteria.where(EngineMemberField.NAME.getName()).is(name));
        Update update = new Update();
        update.inc(taskType);
        update.inc(TASK_COUNT.getName());
        commonRepository.updateField(query, update, EngineMemberEntity.class);
    }

    public void completedTaskRecord(String name, String taskType, Code code) {
        if (StringUtils.isNotBlank(name)) {
            Query query = Query.query(Criteria.where(EngineMemberField.NAME.getName()).is(name));
            Update update = new Update();
            update.inc(taskType, -1);
            if (code != Code.OK) {
                update.set(CONTAINER_STATUS.getName(), ContainerStatus.DIE);
            }
            commonRepository.updateField(query, update, EngineMemberEntity.class);
        }
    }
}
