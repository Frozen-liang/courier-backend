package com.sms.courier.repository;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleSceneCaseJobRepository extends MongoRepository<ScheduleSceneCaseJobEntity, String> {

    List<ScheduleSceneCaseJobEntity> removeByEngineIdInAndJobStatus(List<String> engineIds, JobStatus jobStatus);
}
