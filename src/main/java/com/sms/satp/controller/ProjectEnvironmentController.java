package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.ProjectEnvironmentRequest;
import com.sms.satp.dto.ProjectEnvironmentResponse;
import com.sms.satp.service.ProjectEnvironmentService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
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
    public Boolean add(@Valid @RequestBody ProjectEnvironmentRequest projectEnvironmentRequest) {
        projectEnvironmentService.add(projectEnvironmentRequest);
        return Boolean.TRUE;
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody ProjectEnvironmentRequest projectEnvironmentRequest) {
        projectEnvironmentService.edit(projectEnvironmentRequest);
        return Boolean.TRUE;
    }

    @DeleteMapping("/{ids}")
    public Boolean delete(@PathVariable String[] ids) {
        projectEnvironmentService.delete(ids);
        return Boolean.TRUE;
    }

}