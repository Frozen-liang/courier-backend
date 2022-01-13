package com.sms.courier.repository;

import com.sms.courier.security.oauth.OAuthSettingEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OAuthSettingRepository extends MongoRepository<OAuthSettingEntity, String> {

    Optional<OAuthSettingEntity> findByName(String name);

    boolean existsByName(String name);

}
