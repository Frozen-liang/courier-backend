package com.sms.courier.repository;

import com.sms.courier.entity.api.ApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;

public interface ApiRepository extends MongoRepository<ApiEntity, String> {

    Streamable<ApiEntity> findApiEntitiesByProjectIdAndSwaggerIdNotNull(String projectId);

    void deleteAllByIdIn(List<String> ids);

    List<ApiEntity> deleteAllByProjectIdAndRemovedIsTrue(String projectId);

    ApiEntity findApiEntityByIdAndRemoved(String id, boolean removed);
}
