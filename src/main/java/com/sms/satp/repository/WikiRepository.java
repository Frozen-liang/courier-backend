package com.sms.satp.repository;

import com.sms.satp.entity.Wiki;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WikiRepository extends MongoRepository<Wiki, String> {

}
