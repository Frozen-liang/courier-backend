package com.sms.courier.service;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

public interface ProjectStatisticsService {

    Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request, String countType);

    Long allCount(String projectId, String countType);

    Long caseCount(ObjectId projectId, String countType);

    List<CaseCountStatisticsResponse> groupDayCount(String projectId, Integer day, String groupType);

    List<CaseCountStatisticsResponse> sceneCaseJobGroupDayCount(String projectId, Integer day);
}
