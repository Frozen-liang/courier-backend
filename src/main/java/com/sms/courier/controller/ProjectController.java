package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.PROJECT_PATH;

import com.sms.courier.common.validate.InsertGroup;
import com.sms.courier.common.validate.UpdateGroup;
import com.sms.courier.dto.request.ProjectRequest;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.TestCaseCountStatisticsResponse;
import com.sms.courier.service.ProjectService;
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
@RequestMapping(PROJECT_PATH)
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable("id") String id) {
        return projectService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRoleOrAdmin(@role.PROJECT_CREATE)")
    public Boolean add(@Validated(InsertGroup.class) @RequestBody ProjectRequest projectRequest) {
        return projectService.add(projectRequest);
    }

    @PutMapping
    @PreAuthorize("hasRoleOrAdmin(@role.PROJECT_UPDATE)")
    public Boolean edit(@Validated(UpdateGroup.class) @RequestBody ProjectRequest projectRequest) {
        return projectService.edit(projectRequest);
    }

    @GetMapping("/list/{workspaceId}")
    @PreAuthorize("hasRoleOrAdmin(@role.PROJECT_QUERY_ALL)")
    public List<ProjectResponse> list(@PathVariable String workspaceId) {
        return projectService.list(workspaceId);
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasRoleOrAdmin(@role.PROJECT_DELETE)")
    public Boolean delete(@PathVariable List<String> ids) {
        return projectService.delete(ids);
    }

    @GetMapping("/case/group-day/count/{projectId}")
    public List<TestCaseCountStatisticsResponse> caseGroupDayCount(@PathVariable String projectId) {
        return projectService.caseGroupDayCount(projectId);
    }

}