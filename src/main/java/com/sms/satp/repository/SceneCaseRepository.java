package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseRepository extends MongoRepository<SceneCase, String> {

}
