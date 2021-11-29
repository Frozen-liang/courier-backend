package com.sms.courier.repository;

import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProjectImportSourceRepository extends MongoRepository<ProjectImportSourceEntity, String> {

    ProjectImportSourceResponse findFirstByProjectIdAndRemovedIsFalseOrderByCreateDateTimeDesc(String projectId);
}
