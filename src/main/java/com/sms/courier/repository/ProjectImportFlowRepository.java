package com.sms.courier.repository;

import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.entity.project.ProjectImportFlowEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectImportFlowRepository extends MongoRepository<ProjectImportFlowEntity, String> {

    ProjectImportFlowEntity findFirstByImportSourceIdOrderByCreateDateTimeDesc(String importSourceId);

    ProjectImportFlowResponse findFirstByProjectId(String projectId);

    boolean existsByIdAndImportStatus(String id, int code);
}
