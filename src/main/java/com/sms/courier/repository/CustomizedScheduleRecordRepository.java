package com.sms.courier.repository;

import com.sms.courier.entity.job.ScheduleSceneCaseJobEntity;
import com.sms.courier.entity.schedule.ScheduleRecordEntity;

public interface CustomizedScheduleRecordRepository {

    ScheduleRecordEntity findAndModifyExecuteRecord(String id, ScheduleSceneCaseJobEntity scheduleCaseJob);

}
