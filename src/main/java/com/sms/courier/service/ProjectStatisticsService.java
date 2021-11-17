package com.sms.courier.service;

import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import org.springframework.data.domain.Page;

public interface ProjectStatisticsService {

    Page<ApiPageResponse> sceneCountPage(ApiIncludeCaseRequest request);

    Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request);
}
