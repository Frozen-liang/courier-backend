package com.sms.courier.repository;

import com.sms.courier.entity.schedule.ScheduleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<ScheduleEntity, String> {

}
