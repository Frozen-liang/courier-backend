package com.sms.courier.repository;

import com.sms.courier.entity.workspace.WorkspaceEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<WorkspaceEntity, String> {

    List<WorkspaceEntity> findAllByRemovedIsFalseAndUserIdsContainsOrderByCreateDateTimeDesc(String userId);

    List<WorkspaceEntity> findAllByRemovedIsFalseOrderByCreateDateTimeDesc();
}