package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApiLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseApiLogRepository extends MongoRepository<SceneCaseApiLog, String> {

}
