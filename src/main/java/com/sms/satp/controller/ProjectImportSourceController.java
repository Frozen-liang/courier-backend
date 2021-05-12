package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.PROJECT_IMPORT_SOURCE;

import com.sms.satp.common.validate.UpdateGroup;
import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.dto.response.ProjectImportSourceResponse;
import com.sms.satp.service.ProjectImportSourceService;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PROJECT_IMPORT_SOURCE)
public class ProjectImportSourceController {

    private final ProjectImportSourceService projectImportSourceService;

    public ProjectImportSourceController(ProjectImportSourceService projectImportSourceService) {
        this.projectImportSourceService = projectImportSourceService;
    }

    @PostMapping
    public Boolean create(@Validated @RequestBody ProjectImportSourceRequest request) {
        return projectImportSourceService.create(request);
    }

    @PatchMapping
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody ProjectImportSourceRequest request) {
        return projectImportSourceService.update(request);
    }

    @GetMapping("/{id}")
    public ProjectImportSourceResponse findById(@PathVariable String id) {
        return projectImportSourceService.findById(id);
    }

    @GetMapping("/pid/{projectId}")
    public List<ProjectImportSourceResponse> findByProjectId(@PathVariable String projectId) {
        return projectImportSourceService.findByProjectId(projectId);
    }

}
