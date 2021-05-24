package com.sms.satp.repository;

import com.sms.satp.entity.log.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogEntity, String> {

}
