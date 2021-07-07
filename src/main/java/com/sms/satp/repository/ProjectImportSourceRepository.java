package com.sms.satp.repository;

import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.entity.project.ProjectImportSourceEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProjectImportSourceRepository extends MongoRepository<ProjectImportSourceEntity, String> {

    List<ProjectImportSourceResponse> findByProjectIdAndRemovedIsFalse(String projectId);
}
