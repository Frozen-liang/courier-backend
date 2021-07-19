package com.sms.satp.repository;

import com.sms.satp.entity.function.ProjectFunctionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectFunctionRepository extends MongoRepository<ProjectFunctionEntity, String> {

}