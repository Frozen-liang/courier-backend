package com.sms.courier.repository;

import com.sms.courier.common.enums.JobStatus;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.entity.job.ScheduleCaseJobEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleCaseJobRepository extends MongoRepository<ScheduleCaseJobEntity, String> {

    List<ScheduleCaseJobEntity> findByEngineIdInAndJobStatus(List<String> engineIds, JobStatus jobStatus);

    List<ScheduleCaseJobResponse> findByScheduleRecordIdIs(String scheduleRecordId);
}
