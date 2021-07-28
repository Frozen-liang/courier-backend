package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseApiRepository extends MongoRepository<SceneCaseApiEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<SceneCaseApiEntity> findSceneCaseApiEntitiesBySceneCaseIdAndRemoved(String sceneCaseId, boolean removed);

}
