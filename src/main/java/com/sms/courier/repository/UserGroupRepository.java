package com.sms.courier.repository;

import com.sms.courier.entity.system.UserGroupEntity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserGroupRepository extends MongoRepository<UserGroupEntity, String> {

    List<UserGroupEntity> findAllByRemovedIsFalseOrderByCreateDateTimeDesc();

    boolean existsByDefaultGroupIsTrue();

    Stream<UserGroupEntity> findAllByIdIn(List<String> ids);

    boolean existsByName(String name);
}
