package com.sms.satp.repository;

import com.sms.satp.entity.ApiLabel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiLabelRepository extends MongoRepository<ApiLabel, String> {

}
