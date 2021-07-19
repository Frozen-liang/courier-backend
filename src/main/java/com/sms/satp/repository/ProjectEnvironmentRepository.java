package com.sms.satp.repository;

import com.sms.satp.entity.env.ProjectEnvironmentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEnvironmentRepository extends MongoRepository<ProjectEnvironmentEntity, String> {

    List<ProjectEnvironmentEntity> findAllByProjectIdAndRemoved(String projectId, Boolean removed);
}
