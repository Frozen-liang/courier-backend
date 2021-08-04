package com.sms.courier.repository;

import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.entity.project.ProjectImportSourceEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProjectImportSourceRepository extends MongoRepository<ProjectImportSourceEntity, String> {

    List<ProjectImportSourceResponse> findByProjectIdAndRemovedIsFalse(String projectId);
}
