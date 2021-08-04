package com.sms.courier.repository;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTestCaseJobRepository extends MongoRepository<ApiTestCaseJobEntity, String> {

    List<ApiTestCaseJobEntity> removeByEngineIdInAndJobStatus(List<String> ids, JobStatus jobStatus);
}
