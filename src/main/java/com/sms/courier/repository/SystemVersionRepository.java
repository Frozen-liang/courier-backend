package com.sms.courier.repository;

import com.sms.courier.entity.system.SystemVersionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemVersionRepository extends MongoRepository<SystemVersionEntity, String> {

    SystemVersionEntity findByVersion(String version);
}
