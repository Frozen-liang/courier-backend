package com.sms.courier.repository;

import com.sms.courier.entity.group.ApiTagGroupEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagGroupRepository extends MongoRepository<ApiTagGroupEntity, String> {

    List<ApiTagGroupEntity> findByProjectId(String projectId);

    boolean existsByProjectIdAndName(String projectId, String groupName);

    Long deleteByIdIn(List<String> ids);
}