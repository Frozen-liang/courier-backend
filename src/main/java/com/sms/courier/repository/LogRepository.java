package com.sms.courier.repository;

import com.sms.courier.entity.log.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<LogEntity, String> {

}
