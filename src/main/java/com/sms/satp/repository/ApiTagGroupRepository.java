package com.sms.satp.repository;

import com.sms.satp.entity.group.ApiTagGroup;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagGroupRepository extends MongoRepository<ApiTagGroup, String> {

    List<ApiTagGroup> findByProjectId(String projectId);

    Optional<ApiTagGroup> findByProjectIdAndName(String projectId,String groupName);

    Long deleteByIdIn(List<String> ids);
}