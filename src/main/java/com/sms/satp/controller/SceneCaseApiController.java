package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.AddSceneCaseApiDto;
import com.sms.satp.dto.SceneCaseApiDto;
import com.sms.satp.dto.UpdateSceneCaseApiSortOrderDto;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SCENE_CASE_API_PATH)
public class SceneCaseApiController {

    private final SceneCaseApiService sceneCaseApiService;

    public SceneCaseApiController(SceneCaseApiService sceneCaseApiService) {
        this.sceneCaseApiService = sceneCaseApiService;
    }

    @PostMapping(value = "/batch")
    public Boolean batch(@Valid @RequestBody AddSceneCaseApiDto addSceneCaseApiDto) {
        sceneCaseApiService.batch(addSceneCaseApiDto);
        return Boolean.TRUE;
    }

    @DeleteMapping(value = "/{ids}")
    public Boolean deleteByIds(@PathVariable String[] ids) {
        for (String id : ids) {
            sceneCaseApiService.deleteById(id);
        }
        return Boolean.TRUE;
    }

    @PutMapping
    public Boolean edit(@Valid @RequestBody SceneCaseApiDto sceneCaseApiDto) {
        sceneCaseApiService.edit(sceneCaseApiDto);
        return Boolean.TRUE;
    }

    @PutMapping(value = "/batch/edit")
    public Boolean batchEdit(@Valid @RequestBody UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto) {
        sceneCaseApiService.batchEdit(updateSceneCaseApiSortOrderDto);
        return Boolean.TRUE;
    }

    @GetMapping(value = "/list/{sceneCaseId}/{remove}")
    public List<SceneCaseApiDto> listBySceneCaseId(@PathVariable String sceneCaseId,
        @PathVariable boolean remove) {
        return sceneCaseApiService.listBySceneCaseId(sceneCaseId, remove);
    }

    @GetMapping(value = "/{id}")
    public SceneCaseApiDto getSceneCaseApiById(@PathVariable String id) {
        return sceneCaseApiService.getSceneCaseApiById(id);
    }

}
