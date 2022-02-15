package com.sms.courier.service;

import com.sms.courier.entity.schedule.ScheduleEntity;

public interface ScheduleCaseJobService extends JobService {

    void schedule(ScheduleEntity scheduleEntity,String metadata);
}
