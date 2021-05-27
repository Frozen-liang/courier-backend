package com.sms.satp.repository;

import com.sms.satp.entity.apitestcase.ApiTestCase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTestCaseRepository extends MongoRepository<ApiTestCase, String> {

}