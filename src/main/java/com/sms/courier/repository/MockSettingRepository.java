package com.sms.courier.repository;

import com.sms.courier.dto.response.MockSettingResponse;
import com.sms.courier.entity.mock.MockSettingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MockSettingRepository extends MongoRepository<MockSettingEntity, String> {

    MockSettingResponse getFirstByOrderByModifyDateTimeDesc();

}
