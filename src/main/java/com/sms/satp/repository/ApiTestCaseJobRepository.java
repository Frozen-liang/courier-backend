package com.sms.satp.repository;

import com.sms.satp.entity.job.ApiTestCaseJobEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTestCaseJobRepository extends MongoRepository<ApiTestCaseJobEntity, String> {

}
