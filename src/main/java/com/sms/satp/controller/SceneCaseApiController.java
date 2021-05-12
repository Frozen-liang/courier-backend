package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.response.Response;
import com.sms.satp.dto.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.SceneCaseApiResponse;
import com.sms.satp.dto.UpdateSceneCaseApiRequest;
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
    public Response batchAdd(@Valid @RequestBody BatchAddSceneCaseApiRequest addSceneCaseApiDto) {
        sceneCaseApiService.batchAdd(addSceneCaseApiDto);
        return Response.ok().build();
    }

    @DeleteMapping(value = "/{ids}")
    public Response deleteByIds(@PathVariable List<String> ids) {
        sceneCaseApiService.deleteByIds(ids);
        return Response.ok().build();
    }

    @PutMapping
    public Response edit(@Valid @RequestBody UpdateSceneCaseApiRequest updateSceneCaseApiRequest) {
        sceneCaseApiService.edit(updateSceneCaseApiRequest);
        return Response.ok().build();
    }

    @GetMapping(value = "/{id}")
    public Response<SceneCaseApiResponse> getSceneCaseApiById(@PathVariable String id) {
        return Response.ok(sceneCaseApiService.getSceneCaseApiById(id));
    }

}
