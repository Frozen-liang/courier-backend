package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.dto.request.SceneCaseJobRequest;
import com.sms.satp.dto.response.SceneCaseJobResponse;
import com.sms.satp.service.SceneCaseJobService;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SCENE_CASE_JOB_PATH)
public class SceneCaseJobController {

    private final SceneCaseJobService sceneCaseJobService;

    public SceneCaseJobController(SceneCaseJobService sceneCaseJobService) {
        this.sceneCaseJobService = sceneCaseJobService;
    }

    @GetMapping("/page")
    public Page<SceneCaseJobResponse> page(SceneCaseJobRequest sceneCaseJobRequest) {
        return sceneCaseJobService.page(sceneCaseJobRequest);
    }

    @GetMapping("/{jobId}")
    public SceneCaseJobResponse get(@PathVariable String jobId) {
        return sceneCaseJobService.get(jobId);
    }

}
