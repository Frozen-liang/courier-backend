package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCHEDULE_JOB_PATH;

import com.sms.courier.dto.request.ScheduleJobRequest;
import com.sms.courier.dto.response.ScheduleCaseJobResponse;
import com.sms.courier.dto.response.ScheduleSceneCaseJobResponse;
import com.sms.courier.service.ScheduleJobService;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SCHEDULE_JOB_PATH)
public class ScheduleJobController {

    private final ScheduleJobService scheduleJobService;

    public ScheduleJobController(ScheduleJobService scheduleJobService) {
        this.scheduleJobService = scheduleJobService;
    }

    @PostMapping("/getCaseJobInfo")
    public List<ScheduleCaseJobResponse> getCaseJobInfo(ScheduleJobRequest request) {
        return scheduleJobService.getCaseJobInfoString(request);
    }

    @PostMapping("/getSceneCaseJobInfo")
    public List<ScheduleSceneCaseJobResponse> getSceneCaseJobInfo(ScheduleJobRequest request) {
        return scheduleJobService.getSceneCaseJobInfo(request);
    }
}
