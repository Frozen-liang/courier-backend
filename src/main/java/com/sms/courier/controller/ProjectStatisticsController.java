package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.PROJECT_STATISTICS_PATH;

import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.service.ProjectStatisticsService;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(PROJECT_STATISTICS_PATH)
public class ProjectStatisticsController {

    private final ProjectStatisticsService projectStatisticsService;

    public ProjectStatisticsController(ProjectStatisticsService projectStatisticsService) {
        this.projectStatisticsService = projectStatisticsService;
    }

    @PostMapping("/scene/count/page")
    public Page<ApiPageResponse> sceneCountPage(@Validated @RequestBody ApiIncludeCaseRequest request) {
        return projectStatisticsService.caseCountPage(request, StatisticsCountType.SCENE_CASE.getName());
    }

    @PostMapping("/case/count/page")
    public Page<ApiPageResponse> caseCountPage(@Validated @RequestBody ApiIncludeCaseRequest request) {
        return projectStatisticsService.caseCountPage(request, StatisticsCountType.API_TEST_CASE.getName());
    }

    @GetMapping("/case/group-day/count/{projectId}")
    public List<CountStatisticsResponse> caseGroupDayCount(@PathVariable String projectId) {
        return projectStatisticsService
            .groupDayCount(projectId, Constants.CASE_DAY, StatisticsGroupQueryType.API_TEST_CASE
                .getName());
    }

    @GetMapping("/scene/case/group-day/count/{projectId}")
    public List<CountStatisticsResponse> sceneCaseGroupDayCount(@PathVariable String projectId) {
        return projectStatisticsService.groupDayCount(projectId, Constants.CASE_DAY, StatisticsGroupQueryType.SCENE_CASE
            .getName());
    }

    @GetMapping("/api/all/count/pid/{projectId}")
    public Long apiAllCount(@PathVariable String projectId) {
        return projectStatisticsService.allCount(projectId, StatisticsCountType.API.getName());
    }

    @GetMapping("/scene/all/count/pid/{projectId}")
    public Long sceneAllCount(@PathVariable String projectId) {
        return projectStatisticsService.allCount(projectId, StatisticsCountType.SCENE_CASE.getName());
    }

    @GetMapping("/case/all/count/pid/{projectId}")
    public Long caseAllCount(@PathVariable String projectId) {
        return projectStatisticsService.allCount(projectId, StatisticsCountType.API_TEST_CASE.getName());
    }

    @GetMapping("/api/scene/count/pid/{projectId}")
    public Long sceneCount(@PathVariable ObjectId projectId) {
        return projectStatisticsService.caseCount(projectId, StatisticsCountType.SCENE_CASE.getName());
    }

    @GetMapping("/api/case/count/pid/{projectId}")
    public Long caseCount(@PathVariable ObjectId projectId) {
        return projectStatisticsService.caseCount(projectId, StatisticsCountType.API_TEST_CASE.getName());
    }

    @GetMapping("/case/job/group-day/{day}/count/{projectId}")
    public List<CountStatisticsResponse> caseJobGroupDayCount(@PathVariable String projectId,
        @PathVariable Integer day) {
        return projectStatisticsService.groupDayCount(projectId, day, StatisticsGroupQueryType.API_TEST_CASE_JOB
            .getName());
    }

    @GetMapping("/scene/case/job/group-day/{day}/count/{projectId}")
    public List<CountStatisticsResponse> sceneCaseJobGroupDayCount(@PathVariable String projectId,
        @PathVariable Integer day) {
        return projectStatisticsService.sceneCaseJobGroupDayCount(projectId, day);
    }

}
