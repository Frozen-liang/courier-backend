package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_USER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_COUNT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_JOB_GROUP_BY_USER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR;

import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ProjectService;
import com.sms.courier.service.ProjectStatisticsService;
import com.sms.courier.service.WorkspaceStatisticsService;
import com.sms.courier.utils.NumberUtil;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkspaceStatisticsServiceImpl extends AbstractStatisticsService implements WorkspaceStatisticsService {

    private final ProjectService projectService;
    private final ProjectStatisticsService projectStatisticsService;

    public WorkspaceStatisticsServiceImpl(ProjectService projectService,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedApiRepository customizedApiRepository,
        ProjectStatisticsService projectStatisticsService) {
        super(commonStatisticsRepository, customizedSceneCaseRepository, customizedApiTestCaseRepository,
            customizedApiRepository);
        this.projectService = projectService;
        this.projectStatisticsService = projectStatisticsService;
    }

    @Override
    public Long allCount(String workspaceId, String countType) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(projectResponses)
                ? allCountTypeMap.get(countType).apply(projectIds)
                : 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace count!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_COUNT_ERROR);
        }
    }

    @Override
    public List<CaseCountStatisticsResponse> groupDayCount(String workspaceId, Integer day, String groupType) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupDay(projectIds, day, groupQueryTypeMap.get(groupType));
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR);
        }
    }


    @Override
    public List<CaseCountUserStatisticsResponse> groupUserCount(Integer day, String workspaceId, String groupType) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupUser(projectIds, day, groupQueryTypeMap.get(groupType));
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case group by user!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_GROUP_BY_USER_ERROR);
        }
    }


    @Override
    public List<CaseCountUserStatisticsResponse> groupUserByJob(Integer day, String workspaceId, String groupType) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupUserByJob(projectIds, day, groupQueryTypeMap.get(groupType));
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace job group by user!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_JOB_GROUP_BY_USER_ERROR);
        }
    }


    @Override
    public List<WorkspaceProjectCaseStatisticsResponse> projectPercentage(String workspaceId, String queryType) {
        try {
            List<WorkspaceProjectCaseStatisticsResponse> dto = Lists.newArrayList();
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            for (ProjectResponse project : projectResponses) {
                int caseCountInt =
                    projectStatisticsService.caseCount(new ObjectId(project.getId()), queryType).intValue();
                int apiCountInt =
                    projectStatisticsService.allCount(project.getId(), StatisticsCountType.API.getName()).intValue();
                double caseCount = caseCountInt * 1.0;
                double apiCount = apiCountInt * 1.0;
                double percentage = NumberUtil.getPercentage(caseCount, apiCount);
                WorkspaceProjectCaseStatisticsResponse response = WorkspaceProjectCaseStatisticsResponse.builder()
                    .projectId(project.getId())
                    .projectName(project.getName())
                    .apiCount(apiCountInt)
                    .caseCount(caseCountInt)
                    .percentage(percentage)
                    .build();
                dto.add(response);
            }
            if (CollectionUtils.isNotEmpty(dto)) {
                dto.sort(Comparator.comparingDouble(WorkspaceProjectCaseStatisticsResponse::getPercentage).reversed());
            }
            return dto;
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get workspace project case percentage!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR);
        }
    }

}
