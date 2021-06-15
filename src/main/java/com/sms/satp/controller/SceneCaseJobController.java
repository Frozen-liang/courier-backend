package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.service.SceneCaseJobService;
import java.util.List;
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

    @PostMapping
    public Boolean add(@Valid @RequestBody AddSceneCaseJobRequest request) {
        return sceneCaseJobService.add(request);
    }

    @GetMapping("/page/{sceneCaseId}/{userIds}")
    public Page<SceneCaseJob> page(@PathVariable String sceneCaseId, @PathVariable List<String> userIds,
        PageDto pageDto) {
        return sceneCaseJobService.page(sceneCaseId, userIds, pageDto);
    }

    @GetMapping("/{jobId}")
    public SceneCaseJob get(@PathVariable String jobId) {
        return sceneCaseJobService.get(jobId);
    }

}
