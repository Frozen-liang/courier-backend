package com.sms.satp.repository;

import com.sms.satp.entity.StatusCodeDoc;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatusCodeDocRepository extends MongoRepository<StatusCodeDoc, String> {

}
