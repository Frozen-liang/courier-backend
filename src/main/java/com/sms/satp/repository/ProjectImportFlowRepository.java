package com.sms.satp.repository;

import com.sms.satp.dto.response.ProjectImportFlowResponse;
import com.sms.satp.entity.project.ProjectImportFlowEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectImportFlowRepository extends MongoRepository<ProjectImportFlowEntity, String> {

    ProjectImportFlowEntity findFirstByImportSourceIdOrderByCreateDateTimeDesc(String importSourceId);

    ProjectImportFlowResponse findFirstByProjectId(String projectId);

    boolean existsByIdAndImportStatus(String id, int code);
}
