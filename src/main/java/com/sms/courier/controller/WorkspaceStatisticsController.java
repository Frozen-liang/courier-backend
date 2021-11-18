package com.sms.courier.controller;

import static com.sms.courier.common.constant.Constants.WORKSPACE_STATISTICS_PATH;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
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

    @GetMapping("/case/group-day/{day}/count/{workspaceId}")
    public List<CaseCountStatisticsResponse> caseGroupDayCount(@PathVariable String workspaceId,
        @PathVariable Integer day) {
        return workspaceStatisticsService.caseGroupDayCount(workspaceId, day);
    }

}
