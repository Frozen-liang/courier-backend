package com.sms.courier.repository;

import com.sms.courier.entity.group.ApiGroupEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiGroupRepository extends MongoRepository<ApiGroupEntity, String> {

    Stream<ApiGroupEntity> findAllByPathContains(Long realGroupId);

    void deleteAllByIdIn(List<String> ids);

    List<ApiGroupEntity> findByProjectIdOrderByNameAscCreateDateTimeDesc(String projectId);

    List<ApiGroupEntity> findApiGroupEntitiesByProjectId(String projectId);
}
