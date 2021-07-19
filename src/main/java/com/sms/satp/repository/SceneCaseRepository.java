package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseRepository extends MongoRepository<SceneCaseEntity, String> {

}
