package com.sms.courier.repository;

import com.sms.courier.entity.mock.MockApiEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MockApiRepository extends MongoRepository<MockApiEntity, String> {

}
