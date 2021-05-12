package com.sms.satp.repository;

import com.sms.satp.entity.api.ApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiRepository extends MongoRepository<ApiEntity, String> {

    List<ApiEntity> findApiEntitiesByProjectId(String projectId);

}
