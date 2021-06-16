package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ApiTagGroupRequest;
import com.sms.satp.dto.response.ApiTagGroupResponse;
import com.sms.satp.service.ApiTagGroupService;
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
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ApiTagGroupRequest apiTagGroupRequest) {
        return apiTagGroupService.add(apiTagGroupRequest);
    }

    @PutMapping
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ApiTagGroupRequest apiTagGroupRequest) {
        return apiTagGroupService.edit(apiTagGroupRequest);
    }

    @GetMapping("/list/{projectId}")
    public List<ApiTagGroupResponse> list(@PathVariable("projectId") String projectId) {
        return apiTagGroupService.list(projectId);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable List<String> ids) {
        return apiTagGroupService.delete(ids);
    }
}