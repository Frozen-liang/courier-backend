package com.sms.courier.repository;

import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.entity.group.ScheduleGroupEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleGroupRepository extends MongoRepository<ScheduleGroupEntity, String> {

    List<ScheduleGroupResponse> findByProjectIdIsOrderByName(String projectId);
}
