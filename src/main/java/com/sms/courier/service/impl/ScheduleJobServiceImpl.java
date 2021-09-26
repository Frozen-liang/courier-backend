package com.sms.courier.service.impl;

import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.repository.ScheduleCaseJobRepository;
import com.sms.courier.service.ScheduleJobService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

    private final ScheduleCaseJobRepository scheduleCaseJobRepository;

    public ScheduleJobServiceImpl(ScheduleCaseJobRepository scheduleCaseJobRepository) {
        this.scheduleCaseJobRepository = scheduleCaseJobRepository;
    }

    @Override
    public List<ScheduleCaseJobResponse> getScheduleJobInfo(String scheduleRecordId) {
        return scheduleCaseJobRepository.findByScheduleRecordIdIs(scheduleRecordId);
    }
}
