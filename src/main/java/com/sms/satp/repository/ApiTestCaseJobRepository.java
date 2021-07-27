package com.sms.satp.repository;

import com.sms.satp.common.enums.JobStatus;
import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTestCaseJobRepository extends MongoRepository<ApiTestCaseJobEntity, String> {

    List<ApiTestCaseJobEntity> removeByEngineIdInAndJobStatus(List<String> ids, JobStatus jobStatus);
}
