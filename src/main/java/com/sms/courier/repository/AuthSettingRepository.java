package com.sms.courier.repository;

import com.sms.courier.security.oauth.AuthSettingEntity;
import com.sms.courier.security.oauth.AuthType;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthSettingRepository extends MongoRepository<AuthSettingEntity, String> {

    Optional<AuthSettingEntity> findByAuthType(AuthType authType);
}
