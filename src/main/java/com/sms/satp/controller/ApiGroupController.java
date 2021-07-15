package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.response.ApiGroupResponse;
import com.sms.satp.service.ApiGroupService;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_GROUP_PATH)
public class ApiGroupController {

    private final ApiGroupService apiGroupService;

    public ApiGroupController(ApiGroupService apiGroupService) {
        this.apiGroupService = apiGroupService;
    }

    @GetMapping(value = "/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_GROUP_QUERY_ALL)")
    public List<ApiGroupResponse> list(@PathVariable String projectId) {
        return apiGroupService.list(projectId);
    }

}
