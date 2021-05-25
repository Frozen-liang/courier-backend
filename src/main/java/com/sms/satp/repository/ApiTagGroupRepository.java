package com.sms.satp.repository;

import com.sms.satp.entity.group.ApiTagGroup;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagGroupRepository extends MongoRepository<ApiTagGroup, String> {

    List<ApiTagGroup> findByProjectId(String projectId);

    boolean existsByProjectIdAndName(String projectId, String groupName);

    Long deleteByIdIn(List<String> ids);
}