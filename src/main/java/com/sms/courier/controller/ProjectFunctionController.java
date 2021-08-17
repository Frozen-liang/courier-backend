package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ProjectFunctionRequest;
import com.sms.courier.dto.response.FunctionResponse;
import com.sms.courier.dto.response.LoadFunctionResponse;
import com.sms.courier.dto.response.ProjectFunctionResponse;
import com.sms.courier.service.ProjectFunctionService;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public List<FunctionResponse> list(@PathVariable("projectId") String projectId, @PathVariable String workspaceId,
        String functionKey,
        String functionName) {
        // Query global function and project function. Used for UI display.
        return projectFunctionService.list(projectId, workspaceId, functionKey, functionName);
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

    @GetMapping("/find-all")
    @PreAuthorize("hasRole(@role.PROJECT_FUN_FIND_ALL)")
    public Map<String, List<ProjectFunctionResponse>> findAll() {
        // Query all project function. Used for engine.
        return projectFunctionService.findAll();
    }

    @GetMapping("/pull/{ids}")
    @PreAuthorize("hasRole(@role.PROJECT_FUNCTION_PULL)")
    public List<ProjectFunctionResponse> pullFunction(@PathVariable("ids") List<String> ids) {
        return projectFunctionService.pullFunction(ids);
    }

    @GetMapping("/load")
    public List<LoadFunctionResponse> loadFunction(@RequestParam String workspaceId, @RequestParam String projectId) {
        return projectFunctionService.loadFunction(workspaceId, projectId);
    }
}