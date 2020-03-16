package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.ProjectDto;
import com.sms.satp.service.ProjectService;
import com.sms.satp.utils.PageDtoConverter;
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
@RequestMapping(Constants.PROJECT_PATH)
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/page")
    public Response<Page<ProjectDto>> page(PageDto pageDto) {
        PageDtoConverter.frontMapping(pageDto);
        return Response.ok(projectService.page(pageDto));
    }

    @GetMapping("/{id}")
    public Response<ProjectDto> getById(@PathVariable String id) {
        return Response.ok(projectService.findById(id));
    }

    @PostMapping()
    public Response add(@Valid @RequestBody ProjectDto projectDto) {
        projectService.add(projectDto);
        return Response.ok().build();
    }

    @PutMapping()
    public Response edit(@Valid @RequestBody ProjectDto projectDto) {
        projectService.edit(projectDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response delete(@PathVariable String[] ids) {
        for (String id : ids) {
            projectService.delete(id);
        }
        return Response.ok().build();
    }


}