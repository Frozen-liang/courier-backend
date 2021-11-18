package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.CourierSchedulerRequest;
import com.sms.courier.dto.request.DockerLogRequest;
import com.sms.courier.dto.response.CourierSchedulerResponse;
import com.sms.courier.service.CourierSchedulerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.COURIER_SCHEDULER_PATH)
public class CourierSchedulerController {

    private final CourierSchedulerService courierSchedulerService;

    public CourierSchedulerController(CourierSchedulerService courierSchedulerService) {
        this.courierSchedulerService = courierSchedulerService;
    }

    @PutMapping("/setting")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody CourierSchedulerRequest request) {
        return courierSchedulerService.edit(request);
    }

    @GetMapping("/setting/findOne")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public CourierSchedulerResponse findOne() {
        return courierSchedulerService.findOne();
    }

    @PostMapping("/create")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean createCourierScheduler() {
        return courierSchedulerService.createCourierScheduler();
    }

    @PostMapping("/restart")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean restartCourierScheduler() {
        return courierSchedulerService.restartCourierScheduler();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean deleteCourierScheduler() {
        return courierSchedulerService.deleteCourierScheduler();
    }

    @GetMapping("/queryLog")
    @PreAuthorize("hasRoleOrAdmin(@role.ADMIN)")
    public Boolean queryLog(@Validated DockerLogRequest request) {
        return courierSchedulerService.queryLog(request);
    }

}
