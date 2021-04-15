package com.sms.satp.repository;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceWithOnlyTag;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiInterfaceRepository extends MongoRepository<ApiInterface, ObjectId> {

    List<InterfaceWithOnlyTag> findByProjectId(String projectId);
}
