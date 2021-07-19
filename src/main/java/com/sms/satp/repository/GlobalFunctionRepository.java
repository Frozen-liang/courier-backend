package com.sms.satp.repository;

import com.sms.satp.entity.function.GlobalFunctionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalFunctionRepository extends MongoRepository<GlobalFunctionEntity, String> {

}