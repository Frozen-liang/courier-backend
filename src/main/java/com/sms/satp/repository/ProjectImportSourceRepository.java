package com.sms.satp.repository;

import com.sms.satp.entity.project.ProjectImportSourceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProjectImportSourceRepository extends MongoRepository<ProjectImportSourceEntity, String> {

}
