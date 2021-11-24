package com.sms.courier.service;

import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import java.util.List;

public interface WorkspaceStatisticsService {

    List<CaseCountStatisticsResponse> caseGroupDayCount(String workspaceId, Integer day);

    Long sceneAllCount(String workspaceId);

    Long caseAllCount(String workspaceId);

    Long apiAllCount(String workspaceId);

    List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(String workspaceId, Integer day);
}
