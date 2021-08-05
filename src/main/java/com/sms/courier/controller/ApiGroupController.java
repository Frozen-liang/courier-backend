package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ApiGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.service.ApiGroupService;
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
@RequestMapping(Constants.API_GROUP_PATH)
public class ApiGroupController {

    private final ApiGroupService apiGroupService;

    public ApiGroupController(ApiGroupService apiGroupService) {
        this.apiGroupService = apiGroupService;
    }

    @GetMapping(value = "/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.API_GROUP_QUERY_ALL)")
    public List<TreeResponse> list(@PathVariable String projectId) {
        return apiGroupService.list(projectId);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.API_GROUP_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiGroupRequest request) {
        return apiGroupService.add(request);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.API_GROUP_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiGroupRequest request) {
        return apiGroupService.edit(request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAllRoleOrAdmin(@role.API_GROUP_CRE_UPD_DEL,@role.API_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable String id) {
        return apiGroupService.delete(id);
    }

}
