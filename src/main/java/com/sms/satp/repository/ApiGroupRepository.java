package com.sms.satp.repository;

import com.sms.satp.entity.group.ApiGroupEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiGroupRepository extends MongoRepository<ApiGroupEntity, String> {

    List<ApiGroupEntity> findByProjectIdAndDepth(String project, Integer depth);

    List<ApiGroupEntity> findByParentId(String parentId);

    Stream<ApiGroupEntity> findAllByPathContains(Long realGroupId);

    void deleteAllByIdIn(List<String> ids);

    List<ApiGroupEntity> findApiGroupEntitiesByProjectId(String projectId);
}
