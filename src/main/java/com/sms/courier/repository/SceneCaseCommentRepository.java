package com.sms.courier.repository;

import com.sms.courier.entity.scenetest.SceneCaseCommentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseCommentRepository extends MongoRepository<SceneCaseCommentEntity, String> {

    void deleteByIdIn(List<String> ids);
}
