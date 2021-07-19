package com.sms.satp.repository;

import com.sms.satp.entity.system.SystemVersion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemVersionRepository extends MongoRepository<SystemVersion, String> {

    SystemVersion findByName(String name);
}
