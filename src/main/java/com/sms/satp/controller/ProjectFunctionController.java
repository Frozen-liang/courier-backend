package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.ProjectFunctionRequest;
import com.sms.satp.dto.ProjectFunctionResponse;
import com.sms.satp.service.ProjectFunctionService;
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
@RequestMapping(Constants.PROJECT_FUNCTION_PATH)
public class ProjectFunctionController {

    private final ProjectFunctionService projectFunctionService;

    public ProjectFunctionController(ProjectFunctionService projectFunctionService) {
        this.projectFunctionService = projectFunctionService;
    }

    @GetMapping("/{id}")
    public Response<ProjectFunctionResponse> getById(@PathVariable("id") String id) {
        return Response.ok(projectFunctionService.findById(id));
    }

    @GetMapping("/list/{projectId}")
    public Response<List<Object>> list(@PathVariable("projectId") String projectId, String functionKey,
        String functionName) {
        return Response.ok(projectFunctionService.list(projectId, functionKey, functionName));
    }

    @PostMapping
    public Response<Boolean> add(@Validated @RequestBody ProjectFunctionRequest projectFunctionRequest) {
        projectFunctionService.add(projectFunctionRequest);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Validated @RequestBody ProjectFunctionRequest projectFunctionRequest) {
        projectFunctionService.edit(projectFunctionRequest);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        projectFunctionService.delete(ids);
        return Response.ok(Boolean.TRUE);
    }
}