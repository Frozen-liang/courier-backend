package com.sms.courier.service;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface ProjectStatisticsService {

    Page<ApiPageResponse> sceneCountPage(ApiIncludeCaseRequest request);

    Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request);

    List<CountStatisticsResponse> caseGroupDayCount(String projectId);

    List<CountStatisticsResponse> sceneCaseGroupDayCount(String projectId);

    Long apiAllCount(String projectId);

    Long sceneAllCount(String projectId);

    Long caseAllCount(String projectId);

    Long sceneCount(ObjectId projectId);

    Long caseCount(ObjectId projectId);

    List<CountStatisticsResponse> caseJobGroupDayCount(String projectId, Integer day);

    List<CountStatisticsResponse> sceneCaseJobGroupDayCount(String projectId, Integer day);
}
