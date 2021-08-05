package com.sms.courier.repository;

import com.sms.courier.entity.env.ProjectEnvironmentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectEnvironmentRepository extends MongoRepository<ProjectEnvironmentEntity, String> {

    List<ProjectEnvironmentEntity> findAllByProjectIdAndRemoved(String projectId, Boolean removed);
}
