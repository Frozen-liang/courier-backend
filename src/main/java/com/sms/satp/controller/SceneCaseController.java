package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.AddSceneCaseRequest;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseResponse;
import com.sms.satp.dto.SceneTemplateResponse;
import com.sms.satp.dto.SearchSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneCaseRequest;
import com.sms.satp.dto.UpdateSceneTemplateRequest;
import com.sms.satp.service.SceneCaseService;
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
@RequestMapping(Constants.SCENE_CASE_PATH)
public class SceneCaseController {

    private final SceneCaseService sceneCaseService;

    public SceneCaseController(SceneCaseService sceneCaseService) {
        this.sceneCaseService = sceneCaseService;
    }

    @PostMapping
    public Response add(@Valid @RequestBody AddSceneCaseRequest sceneCaseDto) {
        sceneCaseService.add(sceneCaseDto);
        return Response.ok().build();
    }

    @DeleteMapping("/{ids}")
    public Response deleteByIds(@PathVariable List<String> ids) {
        sceneCaseService.deleteByIds(ids);
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody UpdateSceneCaseRequest sceneCaseDto) {
        sceneCaseService.edit(sceneCaseDto);
        return Response.ok().build();
    }

    @GetMapping("/page/{projectId}")
    public Response<Page<SceneCaseResponse>> page(PageDto pageDto, @PathVariable String projectId) {
        return Response.ok(sceneCaseService.page(pageDto, projectId));
    }

    @GetMapping("/search/{projectId}")
    public Response<Page<SceneCaseResponse>> search(SearchSceneCaseRequest searchDto, @PathVariable String projectId) {
        return Response.ok(sceneCaseService.search(searchDto, projectId));
    }

    @GetMapping("/conn/{id}")
    public Response<SceneTemplateResponse> getConn(@PathVariable String id) {
        return Response.ok(sceneCaseService.getConn(id));
    }

    @PutMapping("/conn/edit")
    public Response editConn(@RequestBody UpdateSceneTemplateRequest updateSceneTemplateRequest) {
        sceneCaseService.editConn(updateSceneTemplateRequest);
        return Response.ok().build();
    }
}
