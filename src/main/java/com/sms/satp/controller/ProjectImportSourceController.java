package com.sms.satp.controller;

import static com.sms.satp.common.constant.Constants.PROJECT_IMPORT_SOURCE;

import com.sms.satp.dto.request.ProjectImportSourceRequest;
import com.sms.satp.service.ProjectImportSourceService;
import org.springframework.validation.annotation.Validated;
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
    public Boolean importDocument(@Validated @RequestBody ProjectImportSourceRequest request) {
        return projectImportSourceService.create(request);
    }

}
