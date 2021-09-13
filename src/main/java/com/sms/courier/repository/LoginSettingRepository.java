package com.sms.courier.repository;

import com.sms.courier.entity.system.LoginSettingEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginSettingRepository extends MongoRepository<LoginSettingEntity, String> {

    Long deleteByIdIn(List<String> ids);
}
