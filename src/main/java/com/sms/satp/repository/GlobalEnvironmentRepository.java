package com.sms.satp.repository;

import com.sms.satp.entity.env.GlobalEnvironment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalEnvironmentRepository extends MongoRepository<GlobalEnvironment, String> {

}
