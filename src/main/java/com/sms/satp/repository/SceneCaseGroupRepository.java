package com.sms.satp.repository;

import com.sms.satp.entity.group.SceneCaseGroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SceneCaseGroupRepository extends MongoRepository<SceneCaseGroupEntity, String> {

}
