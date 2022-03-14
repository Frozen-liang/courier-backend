package com.sms.courier.service;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import java.util.List;

public interface WorkspaceStatisticsService {

    Long allCount(String workspaceId, String countType);

    List<CaseCountStatisticsResponse> groupDayCount(String workspaceId, Integer day, String groupType);

    List<CaseCountUserStatisticsResponse> groupUserCount(Integer day, String workspaceId, String groupType);

    List<CaseCountUserStatisticsResponse> groupUserByJob(Integer day, String workspaceId, String groupType);

    List<WorkspaceProjectCaseStatisticsResponse> projectPercentage(String workspaceId, String queryType);

}
