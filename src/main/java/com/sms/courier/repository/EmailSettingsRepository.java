package com.sms.courier.repository;

import com.sms.courier.entity.system.EmailSettingsEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailSettingsRepository extends MongoRepository<EmailSettingsEntity, String> {

    Boolean deleteByIdIn(List<String> ids);
}
