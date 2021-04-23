package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseApiRepository extends MongoRepository<SceneCaseApi,String> {

}
