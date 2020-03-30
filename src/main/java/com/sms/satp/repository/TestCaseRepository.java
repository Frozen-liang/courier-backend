package com.sms.satp.repository;

import com.sms.satp.entity.test.TestCase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TestCaseRepository extends MongoRepository<TestCase, String> {

}
