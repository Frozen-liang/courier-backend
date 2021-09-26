package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCHEDULE_RECORD_PATH;

import com.sms.courier.dto.request.ScheduleRecordPageRequest;
import com.sms.courier.dto.response.ScheduleRecordResponse;
import com.sms.courier.service.ScheduleRecordService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SCHEDULE_RECORD_PATH)
public class ScheduleRecordController {

    private final ScheduleRecordService scheduleRecordService;

    public ScheduleRecordController(ScheduleRecordService scheduleRecordService) {
        this.scheduleRecordService = scheduleRecordService;
    }

    @PostMapping("/page")
    public Page<ScheduleRecordResponse> page(@Validated ScheduleRecordPageRequest request) {
        return scheduleRecordService.page(request);
    }
}
