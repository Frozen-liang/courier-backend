package com.sms.courier.repository;

import com.sms.courier.common.enums.RoleType;
import com.sms.courier.entity.system.SystemRoleEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SystemRoleRepository extends MongoRepository<SystemRoleEntity, String> {

    Stream<SystemRoleEntity> findAllByRoleType(RoleType roleType);

    Stream<SystemRoleEntity> findAllByIdIn(List<String> ids);

}
