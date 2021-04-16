package com.sms.satp.repository;

import com.sms.satp.entity.api.ApiEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiRepository extends MongoRepository<ApiEntity, String> {

}
