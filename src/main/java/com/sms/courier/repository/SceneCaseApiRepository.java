package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseApiRepository extends MongoRepository<SceneCaseApiEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<SceneCaseApiEntity> findAllByIdIsIn(List<String> ids);

    List<SceneCaseApiEntity> findSceneCaseApiEntitiesBySceneCaseIdAndRemovedOrderByOrder(String sceneCaseId,
        boolean removed);

    Long deleteAllBySceneCaseIdIsIn(List<String> ids);

}
