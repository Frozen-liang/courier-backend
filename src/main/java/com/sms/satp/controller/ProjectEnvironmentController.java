package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.validate.InsertGroup;
import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.ProjectEnvironmentRequest;
import com.sms.satp.dto.response.ProjectEnvironmentResponse;
import com.sms.satp.service.ProjectEnvironmentService;
import java.util.List;
import org.springframework.data.domain.Page;
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
@RequestMapping(Constants.PROJECT_ENVIRONMENT_PATH)
public class ProjectEnvironmentController {

    private final ProjectEnvironmentService projectEnvironmentService;

    public ProjectEnvironmentController(ProjectEnvironmentService projectEnvironmentService) {
        this.projectEnvironmentService = projectEnvironmentService;
    }

    @GetMapping("/page/{projectId}")
    public Page<ProjectEnvironmentResponse> page(PageDto pageDto, @PathVariable String projectId) {
        return projectEnvironmentService.page(pageDto, projectId);
    }

    @GetMapping("/{id}")
    public ProjectEnvironmentResponse getById(@PathVariable String id) {
        return projectEnvironmentService.findById(id);
    }

    @GetMapping("/list/{projectId}")
    public List<Object> list(@PathVariable String projectId) {
        return projectEnvironmentService.list(projectId);
    }

    @PostMapping
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ProjectEnvironmentRequest projectEnvironmentRequest) {
        return projectEnvironmentService.add(projectEnvironmentRequest);
    }

    @PutMapping
    public Boolean edit(
        @Validated(UpdateGroup.class) @RequestBody ProjectEnvironmentRequest projectEnvironmentRequest) {
        return projectEnvironmentService.edit(projectEnvironmentRequest);
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable String[] ids) {
        return projectEnvironmentService.delete(ids);
    }

}