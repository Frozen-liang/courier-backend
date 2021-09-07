package com.sms.courier.repository;

import com.sms.courier.entity.schedule.ServicesEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServicesRepository extends MongoRepository<ServicesEntity, String> {

    Optional<ServicesEntity> findByIpAndPort(String ip, Integer port);
}
