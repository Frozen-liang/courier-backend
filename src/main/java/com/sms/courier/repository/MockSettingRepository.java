package com.sms.courier.repository;

import com.sms.courier.entity.mock.MockSettingEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MockSettingRepository extends MongoRepository<MockSettingEntity, String> {

    Optional<MockSettingEntity> findFirstByOrderByCreateDateTimeDesc();
}
