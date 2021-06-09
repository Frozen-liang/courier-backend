package com.sms.satp.repository;

import com.sms.satp.entity.job.ApiTestCaseJob;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTestCaseJobRepository extends MongoRepository<ApiTestCaseJob, String> {

}
