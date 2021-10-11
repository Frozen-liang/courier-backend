package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.SceneCaseEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseRepository extends MongoRepository<SceneCaseEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);

    List<SceneCaseEntity> findByProjectIdAndRemovedIsFalse(String projectId);

    List<SceneCaseEntity> findByProjectIdAndTagIdInAndPriorityIn(String projectId, List<String> tag,
        List<Integer> priority);

    List<SceneCaseEntity> findByProjectIdAndTagIdIn(String projectId, List<String> tag);

    List<SceneCaseEntity> findByProjectIdAndPriorityIn(String projectId, List<Integer> priority);

    List<SceneCaseEntity> findByIdIn(List<String> caseIds);
}
