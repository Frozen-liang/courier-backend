package com.sms.satp.repository;

import com.sms.satp.entity.system.SystemRoleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemRoleRepository extends MongoRepository<SystemRoleEntity, String> {

}
