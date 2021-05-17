package com.sms.satp.repository;

import com.sms.satp.entity.project.ProjectImportFlowEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectImportFlowRepository extends MongoRepository<ProjectImportFlowEntity, String> {

}
