package com.sms.satp.repository;

import com.sms.satp.entity.workspace.WorkspaceEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<WorkspaceEntity, String> {

    List<WorkspaceEntity> findAllByRemovedIsFalseOrderByCreateDateTimeDesc();
}