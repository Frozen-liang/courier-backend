package com.sms.courier.repository;

import com.sms.courier.entity.project.ProjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEntityRepository extends MongoRepository<ProjectEntity, String> {

}
