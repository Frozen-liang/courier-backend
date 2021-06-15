package com.sms.satp.repository;

import com.sms.satp.entity.project.ProjectEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {

    List<ProjectEntity> findAllByRemovedOrderByCreateDateTimeDesc(Boolean removed);
}
