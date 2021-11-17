package com.sms.courier.service.impl;

import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.service.ProjectStatisticsService;
import com.sms.courier.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectStatisticsServiceImpl implements ProjectStatisticsService {

    private final CustomizedApiRepository customizedApiRepository;

    public ProjectStatisticsServiceImpl(CustomizedApiRepository customizedApiRepository) {
        this.customizedApiRepository = customizedApiRepository;
    }

    @Override
    public Page<ApiPageResponse> sceneCountPage(ApiIncludeCaseRequest request) {
        try {
            return customizedApiRepository.sceneCountPage(request);
        } catch (Exception e) {
            log.error("Failed to get scene count api page!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_SCENE_COUNT_API_PAGE_ERROR);
        }
    }

    @Override
    public Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request) {
        try {
            return customizedApiRepository.caseCountPage(request);
        } catch (Exception e) {
            log.error("Failed to get case count api page!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_CASE_COUNT_API_PAGE_ERROR);
        }
    }
}
