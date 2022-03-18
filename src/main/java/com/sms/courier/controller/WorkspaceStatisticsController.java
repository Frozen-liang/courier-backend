package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.WORKSPACE_STATISTICS_PATH;

import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import com.sms.courier.service.WorkspaceStatisticsService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WORKSPACE_STATISTICS_PATH)
public class WorkspaceStatisticsController {

    private final WorkspaceStatisticsService workspaceStatisticsService;

    public WorkspaceStatisticsController(WorkspaceStatisticsService workspaceStatisticsService) {
        this.workspaceStatisticsService = workspaceStatisticsService;
    }

    @GetMapping("/scene/all/count/{workspaceId}")
    public Long sceneAllCount(@PathVariable String workspaceId) {
        return workspaceStatisticsService.allCount(workspaceId, StatisticsCountType.SCENE_CASE.getName());
    }

    @GetMapping("/case/all/count/{workspaceId}")
    public Long caseAllCount(@PathVariable String workspaceId) {
        return workspaceStatisticsService.allCount(workspaceId, StatisticsCountType.API_TEST_CASE.getName());
    }

    @GetMapping("/api/all/count/{workspaceId}")
    public Long apiAllCount(@PathVariable String workspaceId) {
        return workspaceStatisticsService.allCount(workspaceId, StatisticsCountType.API.getName());
    }

    @GetMapping("/case/group-day/{day}/count/{workspaceId}")
    public List<CountStatisticsResponse> caseGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService
            .groupDayCount(workspaceId, day, StatisticsGroupQueryType.API_TEST_CASE.getName());
    }

    @GetMapping("/scene/case/group-day/{day}/count/{workspaceId}")
    public List<CountStatisticsResponse> sceneCaseGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService
            .groupDayCount(workspaceId, day, StatisticsGroupQueryType.SCENE_CASE.getName());
    }

    @GetMapping("/case/job/group-day/{day}/count/{workspaceId}")
    public List<CountStatisticsResponse> caseJobGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService
            .groupDayCount(workspaceId, day, StatisticsGroupQueryType.API_TEST_CASE_JOB.getName());
    }

    @GetMapping("/scene/case/job/group-day/{day}/count/{workspaceId}")
    public List<CountStatisticsResponse> sceneCaseJobGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService
            .groupDayCount(workspaceId, day, StatisticsGroupQueryType.SCENE_CASE_JOB.getName());
    }


    @GetMapping("/case/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> caseGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService
            .groupUserCount(day, workspaceId, StatisticsGroupQueryType.API_TEST_CASE.getName());
    }

    @GetMapping("/scene/case/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> sceneCaseGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService
            .groupUserCount(day, workspaceId, StatisticsGroupQueryType.SCENE_CASE.getName());
    }

    @GetMapping("/case/job/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> caseJobGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService
            .groupUserByJob(day, workspaceId, StatisticsGroupQueryType.API_TEST_CASE_JOB.getName());
    }

    @GetMapping("/scene/case/job/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> sceneCaseJobGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService
            .groupUserByJob(day, workspaceId, StatisticsGroupQueryType.SCENE_CASE_JOB.getName());
    }

    @GetMapping("/project/case/percentage/{workspaceId}")
    public List<WorkspaceProjectCaseStatisticsResponse> projectCasePercentage(@PathVariable String workspaceId) {
        return workspaceStatisticsService.projectPercentage(workspaceId, StatisticsCountType.API_TEST_CASE.getName());
    }

    @GetMapping("/project/scene/case/percentage/{workspaceId}")
    public List<WorkspaceProjectCaseStatisticsResponse> projectSceneCasePercentage(@PathVariable String workspaceId) {
        return workspaceStatisticsService.projectPercentage(workspaceId, StatisticsCountType.SCENE_CASE.getName());
    }

}
