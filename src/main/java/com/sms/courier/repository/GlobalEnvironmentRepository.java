package com.sms.courier.repository;

import com.sms.courier.entity.env.GlobalEnvironmentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalEnvironmentRepository extends MongoRepository<GlobalEnvironmentEntity, String> {

    List<GlobalEnvironmentEntity> findByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(String workspaceId);
}
