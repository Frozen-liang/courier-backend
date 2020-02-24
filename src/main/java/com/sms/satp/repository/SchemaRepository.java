package com.sms.satp.repository;

import com.sms.satp.entity.Schema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SchemaRepository extends MongoRepository<Schema, String> {

}
