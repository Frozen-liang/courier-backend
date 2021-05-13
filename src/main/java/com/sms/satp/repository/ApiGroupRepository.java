package com.sms.satp.repository;

import com.sms.satp.entity.group.ApiGroupEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiGroupRepository extends MongoRepository<ApiGroupEntity, String> {

    List<ApiGroupEntity> findApiGroupEntitiesByProjectId(String project);

}
