package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiSortOrderDto;
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
    public Response batch(@Valid @RequestBody AddSceneCaseApiDto addSceneCaseApiDto) {
        sceneCaseApiService.batch(addSceneCaseApiDto);
        return Response.ok().build();
    }

    @DeleteMapping(value = "/{ids}")
    public Response deleteByIds(@PathVariable String[] ids) {
        for (String id : ids) {
            sceneCaseApiService.deleteById(id);
        }
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody SceneCaseApiDto sceneCaseApiDto) {
        sceneCaseApiService.edit(sceneCaseApiDto);
        return Response.ok().build();
    }

    @PutMapping(value = "/sort-order")
    public Response editSortOrder(@Valid @RequestBody UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto) {
        sceneCaseApiService.editSortOrder(updateSceneCaseApiSortOrderDto);
        return Response.ok().build();
    }

    @GetMapping(value = "/list/{sceneCaseId}/{remove}")
    public Response<List<SceneCaseApiDto>> listBySceneCaseId(@PathVariable String sceneCaseId,
        @PathVariable boolean remove) {
        return Response.ok(sceneCaseApiService.listBySceneCaseId(sceneCaseId, remove));
    }

    @GetMapping(value = "/{id}")
    public Response<SceneCaseApiDto> getSceneCaseApiById(@PathVariable String id) {
        return Response.ok(sceneCaseApiService.getSceneCaseApiById(id));
    }

}
