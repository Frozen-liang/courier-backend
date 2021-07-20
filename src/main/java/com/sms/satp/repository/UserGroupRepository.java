package com.sms.satp.repository;

import com.sms.satp.entity.system.UserGroupEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupRepository extends MongoRepository<UserGroupEntity, String> {

    List<UserGroupEntity> findAllByRemovedIsFalseOrderByCreateDateTimeDesc();

    boolean existsByDefaultGroupIsTrue();
}
