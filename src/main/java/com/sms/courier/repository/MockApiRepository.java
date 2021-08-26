package com.sms.courier.repository;

import com.sms.courier.entity.mock.MockApiEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MockApiRepository extends MongoRepository<MockApiEntity, String> {

    Long deleteAllByIdIsIn(List<String> ids);
}
