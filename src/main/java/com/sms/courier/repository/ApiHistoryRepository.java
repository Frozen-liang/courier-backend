package com.sms.courier.repository;

import com.sms.courier.entity.api.ApiHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiHistoryRepository extends MongoRepository<ApiHistoryEntity, String> {

}
