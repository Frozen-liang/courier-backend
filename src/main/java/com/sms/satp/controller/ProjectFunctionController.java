package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ProjectFunctionRequest;
import com.sms.satp.dto.response.ProjectFunctionResponse;
import com.sms.satp.service.ProjectFunctionService;
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
@RequestMapping(Constants.PROJECT_FUNCTION_PATH)
public class ProjectFunctionController {

    private final ProjectFunctionService projectFunctionService;

    public ProjectFunctionController(ProjectFunctionService projectFunctionService) {
        this.projectFunctionService = projectFunctionService;
    }

    @GetMapping("/{id}")
    public ProjectFunctionResponse getById(@PathVariable("id") String id) {
        return projectFunctionService.findById(id);
    }

    @GetMapping("/list/{projectId}/{workspaceId}")
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_FUN_QUERY_ALL)")
    public List<Object> list(@PathVariable("projectId") String projectId, @PathVariable String workspaceId,
        String functionKey,
        String functionName) {
        // Query global function and project function. Used for UI display.
        return projectFunctionService.list(projectId, workspaceId, functionKey, functionName);
    }

    @GetMapping("/find-all/{projectId}")
    public List<ProjectFunctionResponse> findAll(@PathVariable("projectId") String projectId, String functionKey,
        String functionName) {
        // Query all project function by project id. Used for engine.
        return projectFunctionService.findAll(projectId, functionKey, functionName);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_FUN_CRE_UPD_DEL)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ProjectFunctionRequest projectFunctionRequest) {
        return projectFunctionService.add(projectFunctionRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_FUN_CRE_UPD_DEL)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ProjectFunctionRequest projectFunctionRequest) {
        return projectFunctionService.edit(projectFunctionRequest);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_FUN_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return projectFunctionService.delete(ids);
    }
}