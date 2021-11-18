package com.sms.courier.repository;

import com.sms.courier.dto.response.CourierSchedulerResponse;
import com.sms.courier.entity.schedule.CourierSchedulerEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourierSchedulerRepository extends MongoRepository<CourierSchedulerEntity, String> {

    Optional<CourierSchedulerEntity> findFirstByOrderByCreateDateTimeDesc();

    Optional<CourierSchedulerResponse> getFirstByOrderByCreateDateTimeDesc();

}
