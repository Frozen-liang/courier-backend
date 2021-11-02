package com.sms.courier.repository;

import com.sms.courier.docker.entity.ContainerSettingEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContainerSettingRepository extends MongoRepository<ContainerSettingEntity, String> {

    Optional<ContainerSettingEntity> getFirstByOrderByModifyDateTimeDesc();

}