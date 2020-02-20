package com.sms.satp.repository;

import com.sms.satp.entity.ApiInterface;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiInterfaceRepository extends MongoRepository<ApiInterface, String> {

}
