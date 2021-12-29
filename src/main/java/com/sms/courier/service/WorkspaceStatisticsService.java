package com.sms.courier.service;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import java.util.List;

public interface WorkspaceStatisticsService {

    List<CaseCountStatisticsResponse> caseGroupDayCount(String workspaceId, Integer day);

    Long sceneAllCount(String workspaceId);

    Long caseAllCount(String workspaceId);

    Long apiAllCount(String workspaceId);

    List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(String workspaceId, Integer day);

    List<CaseCountStatisticsResponse> caseJobGroupDayCount(String workspaceId, Integer day);

    List<CaseCountStatisticsResponse> sceneCaseJobGroupDayCount(String workspaceId, Integer day);

    List<CaseCountUserStatisticsResponse> caseGroupUserCount(Integer day, String workspaceId);

    List<CaseCountUserStatisticsResponse> sceneCaseGroupUserCount(Integer day, String workspaceId);

    List<CaseCountUserStatisticsResponse> caseJobGroupUserCount(Integer day, String workspaceId);

    List<CaseCountUserStatisticsResponse> sceneCaseJobGroupUserCount(Integer day, String workspaceId);

    List<WorkspaceProjectCaseStatisticsResponse> projectCasePercentage(String workspaceId);

    List<WorkspaceProjectCaseStatisticsResponse> projectSceneCasePercentage(String workspaceId);
}
