package com.sms.courier.repository;

import com.sms.courier.entity.schedule.ScheduleEntity;
import java.util.stream.Stream;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<ScheduleEntity, String> {

    Stream<ScheduleEntity> findByGroupId(String groupId);
}
