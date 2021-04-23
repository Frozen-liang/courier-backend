package com.sms.satp.repository;

import com.sms.satp.entity.ApiInterface;
import com.sms.satp.entity.InterfaceWithOnlyTag;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiInterfaceRepository extends MongoRepository<ApiInterface, String> {

    List<InterfaceWithOnlyTag> findByProjectId(String projectId);
}
