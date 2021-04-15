package com.sms.satp.repository;

import com.sms.satp.entity.project.ProjectEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEntityRepository extends MongoRepository<ProjectEntity, String> {

}
