package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.SCHEDULE_PATH;

import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ScheduleListRequest;
import com.sms.satp.dto.request.ScheduleRequest;
import com.sms.satp.dto.response.ScheduleResponse;
import com.sms.satp.service.ScheduleService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SCHEDULE_PATH)
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }


    @GetMapping("{id}")
    public ScheduleResponse getById(@PathVariable("id") String id) {
        return scheduleService.findById(id);
    }

    @PostMapping("/page")
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_QUERY_ALL)")
    public List<ScheduleResponse> list(@RequestBody @Validated ScheduleListRequest request) {
        return scheduleService.list(request);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ScheduleRequest request) {
        return scheduleService.add(request);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ScheduleRequest request) {
        return scheduleService.edit(request);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable("id") String id) {
        return scheduleService.delete(id);
    }

}
