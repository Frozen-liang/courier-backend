package com.sms.satp.repository;

import com.sms.satp.entity.api.ApiHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiHistoryRepository extends MongoRepository<ApiHistoryEntity, String> {

}
