package com.sms.satp.repository;

import com.sms.satp.common.enums.RoleType;
import com.sms.satp.entity.system.SystemRoleEntity;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemRoleRepository extends MongoRepository<SystemRoleEntity, String> {

    Stream<SystemRoleEntity> findAllByRoleType(RoleType roleType);
}
