package com.sms.satp.repository;

import com.sms.satp.entity.job.SceneCaseJobEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseJobRepository extends MongoRepository<SceneCaseJobEntity, String> {

}
