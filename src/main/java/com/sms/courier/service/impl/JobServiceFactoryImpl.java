package com.sms.courier.service.impl;

import com.sms.courier.common.annotation.JobServiceType;
import com.sms.courier.common.enums.JobType;
import com.sms.courier.service.ApiTestCaseJobService;
import com.sms.courier.service.JobService;
import com.sms.courier.service.JobServiceFactory;
import com.sms.courier.service.SceneCaseJobService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

@Service
public class JobServiceFactoryImpl implements JobServiceFactory, ApplicationContextAware {

    private Map<JobType, JobService> jobServiceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        jobServiceMap = applicationContext
            .getBeansWithAnnotation(JobServiceType.class).values().stream()
            .map(JobService.class::cast)
            .collect(
                Collectors.toMap(getJobServiceType(), Function.identity()));
    }

    private Function<JobService, JobType> getJobServiceType() {
        return (job) -> {
            Class<?> clz = job.getClass();
            if (AopUtils.isAopProxy(job)) {
                clz = AopUtils.getTargetClass(job);
            }
            return AnnotationUtils.findAnnotation(clz, JobServiceType.class).type();
        };
    }

    @Override
    public JobService getJobService(JobType jobType) {
        if (!jobServiceMap.containsKey(jobType)) {
            throw new IllegalArgumentException(String.format("The job type [%s] is not supported.", jobType));
        }

        return jobServiceMap.get(jobType);
    }

    @Override
    public ApiTestCaseJobService getApiTestCaseJobService() {
        return (ApiTestCaseJobService) jobServiceMap.get(JobType.CASE);

    }

    @Override
    public SceneCaseJobService getSceneCaseJobService() {
        return (SceneCaseJobService) jobServiceMap.get(JobType.SCENE_CASE);
    }
}
