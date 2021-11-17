package com.sms.courier.service;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface ProjectStatisticsService {

    Page<ApiPageResponse> sceneCountPage(ApiIncludeCaseRequest request);

    Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request);

    List<CaseCountStatisticsResponse> caseGroupDayCount(String projectId);

    List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(String projectId);

    Long apiAllCount(String projectId);

    Long sceneAllCount(String projectId);

    Long caseAllCount(String projectId);

    Long sceneCount(ObjectId projectId);

    Long caseCount(ObjectId projectId);
}
