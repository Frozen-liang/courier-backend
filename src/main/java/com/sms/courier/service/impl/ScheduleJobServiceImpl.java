package com.sms.courier.service.impl;

import com.sms.courier.dto.request.ScheduleJobRequest;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.dto.response.ScheduleSceneCaseJobResponse;
import com.sms.courier.repository.ScheduleCaseJobRepository;
import com.sms.courier.repository.ScheduleSceneCaseJobRepository;
import com.sms.courier.service.ScheduleJobService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private final ScheduleCaseJobRepository scheduleCaseJobRepository;
    private final ScheduleSceneCaseJobRepository scheduleSceneCaseJobRepository;

    public ScheduleJobServiceImpl(ScheduleCaseJobRepository scheduleCaseJobRepository,
        ScheduleSceneCaseJobRepository scheduleSceneCaseJobRepository) {
        this.scheduleCaseJobRepository = scheduleCaseJobRepository;
        this.scheduleSceneCaseJobRepository = scheduleSceneCaseJobRepository;
    }

    @Override
    public List<ScheduleCaseJobResponse> getCaseJobInfoString(ScheduleJobRequest request) {
        return scheduleCaseJobRepository
            .findByScheduleRecordIdAndApiTestCase_JobApiTestCase_Id(request.getScheduleRecordId(), request.getCaseId());
    }

    @Override
    public List<ScheduleSceneCaseJobResponse> getSceneCaseJobInfo(ScheduleJobRequest request) {
        return scheduleSceneCaseJobRepository.findByScheduleRecordIdAndSceneCaseId(request.getScheduleRecordId(),
            request.getCaseId());
    }
}
