package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCHEDULE_GROUP_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.service.ScheduleGroupService;
import java.util.List;
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
@RequestMapping(SCHEDULE_GROUP_PATH)
public class ScheduleGroupController {

    private final ScheduleGroupService scheduleGroupService;

    public ScheduleGroupController(ScheduleGroupService scheduleGroupService) {
        this.scheduleGroupService = scheduleGroupService;
    }

    @GetMapping("/{id}")
    public ScheduleGroupResponse getById(@PathVariable("id") String id) {
        return scheduleGroupService.findById(id);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ScheduleGroupRequest scheduleGroupRequest) {
        return scheduleGroupService.add(scheduleGroupRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ScheduleGroupRequest scheduleGroupRequest) {
        return scheduleGroupService.edit(scheduleGroupRequest);
    }

    @GetMapping("/list")
    public List<ScheduleGroupResponse> list(String projectId) {
        return scheduleGroupService.list(projectId);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable String id) {
        return scheduleGroupService.delete(id);
    }
}
