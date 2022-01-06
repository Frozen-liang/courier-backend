package com.sms.courier.repository;

import com.sms.courier.security.oauth.OAuthSettingEntity;
import com.sms.courier.security.oauth.OAuthType;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthSettingRepository extends MongoRepository<OAuthSettingEntity, String> {

    Optional<OAuthSettingEntity> findByAuthType(OAuthType authType);

    boolean existsByAuthType(OAuthType authType);

}
