package com.sms.satp.repository;

import com.sms.satp.entity.scenetest.SceneCaseApi;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseApiRepository extends MongoRepository<SceneCaseApi, String> {

    Long deleteAllByIdIsIn(List<String> ids);
}
