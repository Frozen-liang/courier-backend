package com.sms.satp.controller;

import com.sms.satp.common.constant.Constants;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.SceneCaseApiLogDto;
import com.sms.satp.service.SceneCaseApiLogService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SCENE_CASE_API_LOG_PATH)
public class SceneCaseApiLogController {

    private final SceneCaseApiLogService sceneCaseApiLogService;

    public SceneCaseApiLogController(SceneCaseApiLogService sceneCaseApiLogService) {
        this.sceneCaseApiLogService = sceneCaseApiLogService;
    }

    @GetMapping(value = "/page/{projectId}")
    public Page<SceneCaseApiLogDto> page(PageDto pageDto, @PathVariable String projectId) {
        return sceneCaseApiLogService.page(pageDto, projectId);
    }

}
