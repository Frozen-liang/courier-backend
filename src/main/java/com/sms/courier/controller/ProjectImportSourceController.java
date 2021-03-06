package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.PROJECT_IMPORT_SOURCE;

import com.google.common.collect.Lists;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ProjectImportFlowPageRequest;
import com.sms.courier.dto.request.ProjectImportSourceRequest;
import com.sms.courier.dto.response.ProjectImportFlowResponse;
import com.sms.courier.dto.response.ProjectImportSourceResponse;
import com.sms.courier.service.ProjectImportSourceService;
import io.undertow.util.ConnectionUtils;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_IMP_SOU_CRE_UPD_DEL)")
    public Boolean create(@Validated @RequestBody ProjectImportSourceRequest request) {
        return projectImportSourceService.create(request);
    }

    @PatchMapping
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_IMP_SOU_CRE_UPD_DEL)")
    public Boolean update(@Validated(UpdateGroup.class) @RequestBody ProjectImportSourceRequest request) {
        return projectImportSourceService.update(request);
    }

    @GetMapping("/{id}")
    public ProjectImportSourceResponse findById(@PathVariable String id) {
        return projectImportSourceService.findById(id);
    }

    @GetMapping("/pid/{projectId}")
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_IMP_SOU_QUERY_ALL)")
    public List<ProjectImportSourceResponse> findByProjectId(@PathVariable String projectId) {
        ProjectImportSourceResponse response = projectImportSourceService.findByProjectId(projectId);
        if (Objects.isNull(response)) {
            return Lists.newArrayList();
        }
        return List.of(response);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.PRO_IMP_SOU_CRE_UPD_DEL)")
    public Boolean delete(@PathVariable List<String> ids) {
        return projectImportSourceService.delete(ids);
    }

    @GetMapping("/flow/pid/{projectId}")
    public ProjectImportFlowResponse getProjectImportFlow(@PathVariable String projectId) {
        return projectImportSourceService.getProjectImportFlow(projectId);
    }

    @PostMapping("/flow/page")
    public Page<ProjectImportFlowResponse> getProjectImportFlow(ProjectImportFlowPageRequest request) {
        return projectImportSourceService.pageProjectImportFlow(request);
    }

}
