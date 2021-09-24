package com.sms.courier.repository;


import com.sms.courier.entity.schedule.ScheduleRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRecordRepository extends MongoRepository<ScheduleRecordEntity, String> {

}
