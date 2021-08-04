package com.sms.satp.repository;

import com.sms.satp.entity.schedule.ScheduleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<ScheduleEntity, String> {

}
