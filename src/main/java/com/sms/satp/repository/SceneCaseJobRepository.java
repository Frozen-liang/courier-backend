package com.sms.satp.repository;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.job.SceneCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseJobRepository extends MongoRepository<SceneCaseJobEntity, String> {

    List<SceneCaseJobEntity> removeByEngineIdInAndJobStatus(List<String> engineId, JobStatus jobStatus);
}
