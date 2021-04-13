package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.scenetest.AddSceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCaseDto;
import com.sms.satp.entity.scenetest.SceneCaseSearchDto;
import com.sms.satp.entity.scenetest.UpdateSceneCaseDto;
import com.sms.satp.service.SceneCaseService;
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
@RequestMapping(Constants.SCENE_CASE_PATH)
public class SceneCaseController {

    private final SceneCaseService sceneCaseService;

    public SceneCaseController(SceneCaseService sceneCaseService) {
        this.sceneCaseService = sceneCaseService;
    }

    @PostMapping
    public Response add(@Valid @RequestBody AddSceneCaseDto sceneCaseDto) {
        sceneCaseService.add(sceneCaseDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{id}")
    public Response deleteById(@PathVariable String id) {
        sceneCaseService.deleteById(id);
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody UpdateSceneCaseDto sceneCaseDto) {
        sceneCaseService.edit(sceneCaseDto);
        return Response.ok().build();
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<SceneCaseDto>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(sceneCaseService.page(pageDto, projectId));
    }

    @GetMapping("/search/{projectId}")
    public Response<Page<SceneCaseDto>> search(SceneCaseSearchDto searchDto, @PathVariable String projectId) {
        return Response.ok(sceneCaseService.search(searchDto, projectId));
    }

}
