package com.sms.satp.repository;

import com.sms.satp.entity.job.SceneCaseJob;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseJobRepository extends MongoRepository<SceneCaseJob, String> {

}
