package com.sms.courier.repository;

import com.sms.courier.dto.response.AmazonStorageSettingResponse;
import com.sms.courier.entity.file.AmazonStorageSettingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AmazonStorageSettingRepository extends MongoRepository<AmazonStorageSettingEntity, Object> {

    Optional<AmazonStorageSettingResponse> findFirstByOrderByModifyDateTime();

    Optional<AmazonStorageSettingEntity> getFirstByOrderByModifyDateTime();
}
