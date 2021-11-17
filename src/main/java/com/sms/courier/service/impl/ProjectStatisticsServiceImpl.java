package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_PROJECT_CASE_GROUP_BY_DAY_ERROR;

import com.google.common.collect.Lists;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ApiService;
import com.sms.courier.service.ApiTestCaseService;
import com.sms.courier.service.ProjectStatisticsService;
import com.sms.courier.service.SceneCaseService;
import com.sms.courier.utils.ExceptionUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProjectStatisticsServiceImpl implements ProjectStatisticsService {

    private final CustomizedApiRepository customizedApiRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final ApiService apiService;
    private final SceneCaseService sceneCaseService;
    private final ApiTestCaseService apiTestCaseService;

    public ProjectStatisticsServiceImpl(CustomizedApiRepository customizedApiRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository, ApiService apiService,
        SceneCaseService sceneCaseService, ApiTestCaseService apiTestCaseService) {
        this.customizedApiRepository = customizedApiRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.apiService = apiService;
        this.sceneCaseService = sceneCaseService;
        this.apiTestCaseService = apiTestCaseService;
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

    @Override
    public List<CaseCountStatisticsResponse> caseGroupDayCount(String projectId) {
        try {
            LocalDateTime dateTime = LocalDateTime.now().minusDays(Constants.CASE_DAY);
            List<CaseCountStatisticsResponse> responses = customizedApiTestCaseRepository
                .getCaseGroupDayCount(Lists.newArrayList(projectId), dateTime);
            return handleResponses(responses);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Project case group by day!", e);
            throw new ApiTestPlatformException(GET_PROJECT_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(String projectId) {
        try {
            LocalDateTime dateTime = LocalDateTime.now().minusDays(Constants.CASE_DAY);
            List<CaseCountStatisticsResponse> responses = customizedSceneCaseRepository
                .getSceneCaseGroupDayCount(Lists.newArrayList(projectId), dateTime);
            return handleResponses(responses);
        } catch (Exception e) {
            log.error("Failed to get the Project case group by day!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_SCENE_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public Long apiAllCount(String projectId) {
        try {
            return apiService.count(projectId);
        } catch (Exception e) {
            log.error("Failed to get the Project api count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_API_COUNT_ERROR);
        }
    }

    @Override
    public Long sceneAllCount(String projectId) {
        try {
            return sceneCaseService.count(projectId);
        } catch (Exception e) {
            log.error("Failed to get the Project scene count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_SCENE_COUNT_ERROR);
        }
    }

    @Override
    public Long caseAllCount(String projectId) {
        try {
            return apiTestCaseService.count(projectId);
        } catch (Exception e) {
            log.error("Failed to get the Project api case count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_API_CASE_COUNT_ERROR);
        }
    }

    @Override
    public Long sceneCount(ObjectId projectId) {
        try {
            return customizedApiRepository.sceneCount(projectId);
        } catch (Exception e) {
            log.error("Failed to query scene count the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.GET_SCENE_COUNT_BY_API_ERROR);
        }
    }

    @Override
    public Long caseCount(ObjectId projectId) {
        try {
            return customizedApiRepository.caseCount(projectId);
        } catch (Exception e) {
            log.error("Failed to query case count the Api!", e);
            throw new ApiTestPlatformException(ErrorCode.GET_CASE_COUNT_BY_API_ERROR);
        }
    }

    private List<CaseCountStatisticsResponse> handleResponses(
        List<CaseCountStatisticsResponse> caseCountStatisticsResponses) {
        List<CaseCountStatisticsResponse> responses = Lists.newArrayList(caseCountStatisticsResponses);
        List<LocalDate> localDateList = responses.stream().map(CaseCountStatisticsResponse::getDay)
            .collect(Collectors.toList());
        responses.addAll(IntStream.range(0, 7)
                .mapToObj(i -> LocalDate.now().minusDays(i))
                .filter(i -> !localDateList.contains(i))
                .map(data -> CaseCountStatisticsResponse.builder().day(data).count(0).build())
                .collect(Collectors.toList()));
        responses.sort(Comparator.comparing(CaseCountStatisticsResponse::getDay));
        return responses;
    }

}
