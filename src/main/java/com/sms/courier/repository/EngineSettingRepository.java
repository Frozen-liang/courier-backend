package com.sms.courier.repository;

import com.sms.courier.dto.response.EngineSettingResponse;
import com.sms.courier.engine.model.EngineSettingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EngineSettingRepository extends MongoRepository<EngineSettingEntity, String> {

    EngineSettingResponse getFirstByOrderByModifyDateTimeDesc();

}