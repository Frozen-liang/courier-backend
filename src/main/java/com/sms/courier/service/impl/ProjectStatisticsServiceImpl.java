package com.sms.courier.service.impl;

import com.google.common.collect.Lists;
import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.request.ApiIncludeCaseRequest;
import com.sms.courier.dto.response.ApiPageResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseJobRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ProjectStatisticsService;
import com.sms.courier.utils.ExceptionUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProjectStatisticsServiceImpl extends AbstractStatisticsService implements ProjectStatisticsService {

    private final CustomizedApiRepository customizedApiRepository;
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository;

    public ProjectStatisticsServiceImpl(CustomizedApiRepository customizedApiRepository,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository) {
        super(commonStatisticsRepository, customizedSceneCaseRepository, customizedApiTestCaseRepository,
            customizedApiRepository);
        this.customizedApiRepository = customizedApiRepository;
        this.customizedSceneCaseJobRepository = customizedSceneCaseJobRepository;
    }

    @Override
    public Page<ApiPageResponse> caseCountPage(ApiIncludeCaseRequest request, String countType) {
        try {
            return Objects.equals(StatisticsCountType.SCENE_CASE.getName(), countType)
                ? customizedApiRepository.sceneCountPage(request) : customizedApiRepository.caseCountPage(request);
        } catch (Exception e) {
            log.error("Failed to get case count api page!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_CASE_COUNT_API_PAGE_ERROR);
        }
    }


    @Override
    public Long allCount(String projectId, String countType) {
        try {
            return allCountTypeMap.get(countType).apply(Lists.newArrayList(projectId));
        } catch (Exception e) {
            log.error("Failed to get the Project api count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_API_COUNT_ERROR);
        }
    }


    @Override
    public Long caseCount(ObjectId projectId, String countType) {
        try {
            return Objects.equals(StatisticsCountType.SCENE_CASE.getName(), countType)
                ? customizedApiRepository.sceneCount(projectId) : customizedApiRepository.caseCount(projectId);
        } catch (Exception e) {
            log.error("Failed to query case count the Api!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_CASE_COUNT_BY_API_ERROR);
        }
    }

    @Override
    public List<CountStatisticsResponse> groupDayCount(String projectId, Integer day, String groupType) {
        try {
            return groupDay(Lists.newArrayList(projectId), day,
                groupQueryTypeMap.get(groupType));
        } catch (Exception e) {
            log.error("Failed to get the Project case group by day!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_PROJECT_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public List<CountStatisticsResponse> sceneCaseJobGroupDayCount(String projectId, Integer day) {
        try {
            LocalDateTime dateTime = LocalDateTime.now().minusDays(day);
            List<CountStatisticsResponse> responses = customizedSceneCaseJobRepository
                .getGroupDayCount(Lists.newArrayList(projectId), dateTime);
            return handleResponses(responses, day);
        } catch (Exception e) {
            log.error("Failed to get scene case job count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_SCENE_CASE_JOB_COUNT_ERROR);
        }
    }

}
