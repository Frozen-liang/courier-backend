package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.WORKSPACE_STATISTICS_PATH;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
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
        return workspaceStatisticsService.sceneAllCount(workspaceId);
    }

    @GetMapping("/case/all/count/{workspaceId}")
    public Long caseAllCount(@PathVariable String workspaceId) {
        return workspaceStatisticsService.caseAllCount(workspaceId);
    }

    @GetMapping("/api/all/count/{workspaceId}")
    public Long apiAllCount(@PathVariable String workspaceId) {
        return workspaceStatisticsService.apiAllCount(workspaceId);
    }

    @GetMapping("/case/group-day/{day}/count/{workspaceId}")
    public List<CaseCountStatisticsResponse> caseGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService.caseGroupDayCount(workspaceId, day);
    }

    @GetMapping("/scene/case/group-day/{day}/count/{workspaceId}")
    public List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService.sceneCaseGroupDayCount(workspaceId, day);
    }

    @GetMapping("/case/job/group-day/{day}/count/{workspaceId}")
    public List<CaseCountStatisticsResponse> caseJobGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService.caseJobGroupDayCount(workspaceId, day);
    }

    @GetMapping("/scene/case/job/group-day/{day}/count/{workspaceId}")
    public List<CaseCountStatisticsResponse> sceneCaseJobGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService.sceneCaseJobGroupDayCount(workspaceId, day);
    }

    @GetMapping("/case/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> caseGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService.caseGroupUserCount(day, workspaceId);
    }

    @GetMapping("/scene/case/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> sceneCaseGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService.sceneCaseGroupUserCount(day, workspaceId);
    }

    @GetMapping("/case/job/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> caseJobGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService.caseJobGroupUserCount(day, workspaceId);
    }

    @GetMapping("/scene/case/job/group-user/{day}/count/{workspaceId}")
    public List<CaseCountUserStatisticsResponse> sceneCaseJobGroupUserCount(@PathVariable Integer day,
        @PathVariable String workspaceId) {
        return workspaceStatisticsService.sceneCaseJobGroupUserCount(day, workspaceId);
    }

    @GetMapping("/project/case/percentage/{workspaceId}")
    public List<WorkspaceProjectCaseStatisticsResponse> projectCasePercentage(@PathVariable String workspaceId) {
        return workspaceStatisticsService.projectCasePercentage(workspaceId);
    }

    @GetMapping("/project/scene/case/percentage/{workspaceId}")
    public List<WorkspaceProjectCaseStatisticsResponse> projectSceneCasePercentage(@PathVariable String workspaceId) {
        return workspaceStatisticsService.projectSceneCasePercentage(workspaceId);
    }
}
