package com.sms.courier.repository;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseJobRepository extends MongoRepository<SceneCaseJobEntity, String> {

    List<SceneCaseJobEntity> removeByEngineIdInAndJobStatus(List<String> engineId, JobStatus jobStatus);
}
