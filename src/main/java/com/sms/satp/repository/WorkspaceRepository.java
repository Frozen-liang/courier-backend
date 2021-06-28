package com.sms.satp.repository;

import com.sms.satp.entity.workspace.Workspace;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {

    List<Workspace> findAllByRemovedIsFalseOrderByCreateDateTimeDesc();
}