package com.sms.satp.repository;

import com.sms.satp.entity.function.GlobalFunction;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GlobalFunctionRepository extends MongoRepository<GlobalFunction, String> {

}