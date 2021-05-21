package com.sms.satp.repository;

import com.sms.satp.entity.api.ApiEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;

public interface ApiRepository extends MongoRepository<ApiEntity, String> {

    Streamable<ApiEntity> findApiEntitiesByProjectIdAndSwaggerIdNotNull(String projectId);

}
