package com.sms.courier.repository;

import com.sms.courier.entity.project.ProjectEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<ProjectEntity, String> {

    List<ProjectEntity> findAllByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(String workspaceId);

    boolean existsByWorkspaceIdAndRemovedIsFalse(String workspaceId);

    List<ProjectEntity> findAllByWorkspaceId(String workspaceId);
}
