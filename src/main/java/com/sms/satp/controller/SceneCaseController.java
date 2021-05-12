package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
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
    public Boolean add(@Valid @RequestBody AddSceneCaseRequest sceneCaseDto) {
        return sceneCaseService.add(sceneCaseDto);
    }

    @DeleteMapping("/{ids}")
    public Boolean deleteByIds(@PathVariable List<String> ids) {
        return sceneCaseService.deleteByIds(ids);
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody UpdateSceneCaseRequest sceneCaseDto) {
        return sceneCaseService.edit(sceneCaseDto);
    }

    @GetMapping("/page/{projectId}")
    public Page<SceneCaseResponse> page(PageDto pageDto, @PathVariable String projectId) {
        return sceneCaseService.page(pageDto, projectId);
    }

    @GetMapping("/search/{projectId}")
    public Page<SceneCaseResponse> search(SearchSceneCaseRequest searchDto, @PathVariable String projectId) {
        return sceneCaseService.search(searchDto, projectId);
    }

    @GetMapping("/conn/{id}")
    public SceneTemplateResponse getConn(@PathVariable String id) {
        return sceneCaseService.getConn(id);
    }

    @PutMapping("/conn/edit")
    public Boolean editConn(@RequestBody UpdateSceneTemplateRequest updateSceneTemplateRequest) {
        return sceneCaseService.editConn(updateSceneTemplateRequest);
    }
}
