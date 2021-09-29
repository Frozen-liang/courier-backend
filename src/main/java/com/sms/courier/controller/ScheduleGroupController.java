package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.SCHEDULE_GROUP_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ScheduleGroupRequest;
import com.sms.courier.dto.response.ScheduleGroupResponse;
import com.sms.courier.service.ScheduleGroupService;
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
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_GROUP_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ScheduleGroupRequest scheduleGroupRequest) {
        return scheduleGroupService.add(scheduleGroupRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_GROUP_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ScheduleGroupRequest scheduleGroupRequest) {
        return scheduleGroupService.edit(scheduleGroupRequest);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRoleOrAdmin(@role.SCHEDULE_GROUP_QUERY_ALL)")
    public List<ScheduleGroupResponse> list(String projectId) {
        return scheduleGroupService.list(projectId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAllRoleOrAdmin(@role.SCHEDULE_GROUP_CRE_UPD_DEL,@role.SCHEDULE_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable String id) {
        return scheduleGroupService.delete(id);
    }
}
