package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ApiTagGroupRequest;
import com.sms.courier.dto.response.ApiTagGroupResponse;
import com.sms.courier.service.ApiTagGroupService;
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
@RequestMapping(Constants.API_TAG_GROUP_PATH)
public class ApiTagGroupController {

    private final ApiTagGroupService apiTagGroupService;

    public ApiTagGroupController(ApiTagGroupService apiTagGroupService) {
        this.apiTagGroupService = apiTagGroupService;
    }

    @GetMapping("/{id}")
    public ApiTagGroupResponse getById(@PathVariable("id") String id) {
        return apiTagGroupService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_GROUP_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTagGroupRequest apiTagGroupRequest) {
        return apiTagGroupService.add(apiTagGroupRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_GROUP_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTagGroupRequest apiTagGroupRequest) {
        return apiTagGroupService.edit(apiTagGroupRequest);
    }

    @GetMapping("/list/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_GROUP_QUERY_ALL)")
    public List<ApiTagGroupResponse> list(@PathVariable("projectId") String projectId) {
        return apiTagGroupService.list(projectId);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.TAG_GROUP_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTagGroupService.delete(ids);
    }
}