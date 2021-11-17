package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.PROJECT_STATISTICS;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.service.ProjectStatisticsService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PROJECT_STATISTICS)
public class ProjectStatisticsController {

    private final ProjectStatisticsService projectStatisticsService;

    public ProjectStatisticsController(ProjectStatisticsService projectStatisticsService) {
        this.projectStatisticsService = projectStatisticsService;
    }

    @PostMapping("/scene/count/page")
    public Page<ApiPageResponse> sceneCountPage(@Validated @RequestBody ApiIncludeCaseRequest request) {
        return projectStatisticsService.sceneCountPage(request);
    }

    @PostMapping("/case/count/page")
    public Page<ApiPageResponse> caseCountPage(@Validated @RequestBody ApiIncludeCaseRequest request) {
        return projectStatisticsService.caseCountPage(request);
    }

}
