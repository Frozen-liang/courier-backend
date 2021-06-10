package com.sms.satp.repository;

import com.sms.satp.entity.env.ProjectEnvironment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEnvironmentRepository extends MongoRepository<ProjectEnvironment, String> {

    List<ProjectEnvironment> findAllByProjectId(String projectId);
}
