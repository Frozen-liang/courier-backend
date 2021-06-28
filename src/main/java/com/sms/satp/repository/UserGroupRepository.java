package com.sms.satp.repository;

import com.sms.satp.entity.system.UserGroupEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupRepository extends MongoRepository<UserGroupEntity, String> {

}
