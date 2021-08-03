package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.SceneCaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseRepository extends MongoRepository<SceneCaseEntity, String> {

}
