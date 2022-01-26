package com.sms.courier.repository;

import com.sms.courier.entity.openapi.OpenApiSettingEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OpenApiSettingRepository extends MongoRepository<OpenApiSettingEntity, String> {

    Long deleteByIdIn(List<String> ids);

    boolean existsByIdAndRemovedIsFalseAndExpireTimeGreaterThanEqual(String id, LocalDateTime now);

}
