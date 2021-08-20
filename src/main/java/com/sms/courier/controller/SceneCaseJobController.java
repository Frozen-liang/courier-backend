package com.sms.courier.controller;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.dto.request.AddSceneCaseJobRequest;
import com.sms.courier.dto.request.SceneCaseJobRequest;
import com.sms.courier.dto.response.SceneCaseJobResponse;
import com.sms.courier.entity.job.SceneCaseJobReport;
import com.sms.courier.service.SceneCaseJobService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/buildJob")
    public List<SceneCaseJobResponse> buildJob(@RequestBody AddSceneCaseJobRequest sceneCaseJobRequest) {
        return sceneCaseJobService.buildJob(sceneCaseJobRequest);
    }

    @PutMapping
    public Boolean editReport(@RequestBody SceneCaseJobReport sceneCaseJobReport) {
        return sceneCaseJobService.editReport(sceneCaseJobReport);
    }

}
