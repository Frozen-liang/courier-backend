package com.sms.satp.repository;

import com.sms.satp.entity.tag.ApiTag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiTagRepository extends MongoRepository<ApiTag, String> {

}
