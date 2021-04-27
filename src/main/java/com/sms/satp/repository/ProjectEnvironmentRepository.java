package com.sms.satp.repository;

import com.sms.satp.entity.env.ProjectEnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEnvironmentRepository extends MongoRepository<ProjectEnvironment, String> {

}
