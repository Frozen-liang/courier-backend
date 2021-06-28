package com.sms.satp.repository;

import com.sms.satp.entity.env.GlobalEnvironment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalEnvironmentRepository extends MongoRepository<GlobalEnvironment, String> {

    List<GlobalEnvironment> findByRemovedIsFalseAndWorkspaceIdOrderByCreateDateTimeDesc(String workspaceId);
}
