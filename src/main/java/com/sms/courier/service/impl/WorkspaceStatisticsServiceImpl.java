package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR;

import com.google.common.collect.Lists;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.service.ProjectService;
import com.sms.courier.service.WorkspaceStatisticsService;
import com.sms.courier.utils.ExceptionUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkspaceStatisticsServiceImpl extends AbstractStatisticsService implements WorkspaceStatisticsService {

    private final ProjectService projectService;
    private final CommonStatisticsRepository commonStatisticsRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;

    public WorkspaceStatisticsServiceImpl(ProjectService projectService,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository) {
        this.projectService = projectService;
        this.commonStatisticsRepository = commonStatisticsRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
    }

    @Override
    public List<CaseCountStatisticsResponse> caseGroupDayCount(String workspaceId, Integer day) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            if (CollectionUtils.isNotEmpty(projectResponses)) {
                List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                    .collect(Collectors.toList());
                LocalDateTime dateTime = LocalDateTime.now().minusDays(day);
                List<CaseCountStatisticsResponse> responses = commonStatisticsRepository
                    .getGroupDayCount(projectIds, dateTime, ApiTestCaseEntity.class);
                return handleResponses(responses, day);
            }
            List<CaseCountStatisticsResponse> responses = Lists.newArrayList();
            return handleResponses(responses, day);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public Long caseAllCount(String workspaceId) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            if (CollectionUtils.isNotEmpty(projectResponses)) {
                List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                    .collect(Collectors.toList());
                return customizedApiTestCaseRepository.count(projectIds);
            }
            return 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace api case count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_WORKSPACE_API_CASE_COUNT_ERROR);
        }
    }


}
