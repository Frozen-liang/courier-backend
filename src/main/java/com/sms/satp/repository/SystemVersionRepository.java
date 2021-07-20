package com.sms.satp.repository;

import com.sms.satp.entity.system.SystemVersionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemVersionRepository extends MongoRepository<SystemVersionEntity, String> {

    SystemVersionEntity findByName(String name);
}
