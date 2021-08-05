package com.sms.courier.repository;

import com.sms.courier.entity.group.SceneCaseGroupEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseGroupRepository extends MongoRepository<SceneCaseGroupEntity, String> {

    Stream<SceneCaseGroupEntity> findAllByPathContains(Long realGroupId);

    void deleteAllByIdIn(List<String> ids);

    List<SceneCaseGroupEntity> findSceneCaseGroupEntitiesByProjectId(String projectId);
}
