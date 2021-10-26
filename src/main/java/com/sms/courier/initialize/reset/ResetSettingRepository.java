package com.sms.courier.initialize.reset;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetSettingRepository extends MongoRepository<ResetSetting, String> {

    ResetSetting findByResetType(String type);
}