package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.ProjectEnvironmentDto;
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
    public Response<Page<ProjectEnvironmentDto>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(projectEnvironmentService.page(pageDto, projectId));
    }

    @GetMapping("/{id}")
    public Response<ProjectEnvironmentDto> getById(@PathVariable String id) {
        return Response.ok(projectEnvironmentService.findById(id));
    }

    @GetMapping("/list/{projectId}")
    public Response<List<Object>> list(@PathVariable String projectId) {
        return Response.ok(projectEnvironmentService.list(projectId));
    }

    @PostMapping
    public Response<Boolean> add(@Valid @RequestBody ProjectEnvironmentDto projectEnvironmentDto) {
        projectEnvironmentService.add(projectEnvironmentDto);
        return Response.ok(Boolean.TRUE);
    }

    @PutMapping
    public Response<Boolean> edit(@Valid @RequestBody ProjectEnvironmentDto projectEnvironmentDto) {
        projectEnvironmentService.edit(projectEnvironmentDto);
        return Response.ok(Boolean.TRUE);
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> delete(@PathVariable String[] ids) {
        projectEnvironmentService.delete(ids);
        return Response.ok(Boolean.TRUE);
    }

}