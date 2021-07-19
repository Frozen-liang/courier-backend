package com.sms.satp.repository;

import com.sms.satp.entity.env.GlobalEnvironmentEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalEnvironmentRepository extends MongoRepository<GlobalEnvironmentEntity, String> {

    List<GlobalEnvironmentEntity> findByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(String workspaceId);
}
