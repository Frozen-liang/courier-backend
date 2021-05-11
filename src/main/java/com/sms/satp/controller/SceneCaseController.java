package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.AddSceneCaseDto;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseDto;
import com.sms.satp.dto.SceneCaseSearchDto;
import com.sms.satp.dto.UpdateSceneCaseDto;
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
    public Boolean add(@Valid @RequestBody AddSceneCaseDto sceneCaseDto) {
        sceneCaseService.add(sceneCaseDto);
        return Boolean.TRUE;
    }

    @DeleteMapping("/{ids}")
    public Boolean deleteByIds(@PathVariable String[] ids) {
        for (String id : ids) {
            sceneCaseService.deleteById(id);
        }
        return Boolean.TRUE;
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody UpdateSceneCaseDto sceneCaseDto) {
        sceneCaseService.edit(sceneCaseDto);
        return Boolean.TRUE;
    }

    @GetMapping("/page/{projectId}")
    public Page<SceneCaseDto> page(PageDto pageDto, @PathVariable String projectId) {
        return sceneCaseService.page(pageDto, projectId);
    }

    @GetMapping("/search/{projectId}")
    public Page<SceneCaseDto> search(SceneCaseSearchDto searchDto, @PathVariable String projectId) {
        return sceneCaseService.search(searchDto, projectId);
    }

}
