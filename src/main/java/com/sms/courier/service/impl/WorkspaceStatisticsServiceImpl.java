package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_JOB_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_SCENE_CASE_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_SCENE_CASE_JOB_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_SCENE_COUNT_ERROR;

import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.exception.ErrorCode;
import com.sms.courier.dto.response.CaseCountStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ProjectService;
import com.sms.courier.service.WorkspaceStatisticsService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkspaceStatisticsServiceImpl extends AbstractStatisticsService implements WorkspaceStatisticsService {

    private final ProjectService projectService;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final CustomizedApiRepository customizedApiRepository;

    public WorkspaceStatisticsServiceImpl(ProjectService projectService,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedApiRepository customizedApiRepository) {
        super(commonStatisticsRepository);
        this.projectService = projectService;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.customizedApiRepository = customizedApiRepository;
    }

    @Override
    public List<CaseCountStatisticsResponse> caseGroupDayCount(String workspaceId, Integer day) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupDay(projectIds, day, ApiTestCaseEntity.class);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public Long sceneAllCount(String workspaceId) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(projectResponses) ? customizedSceneCaseRepository.count(projectIds) : 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace scene count!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_SCENE_COUNT_ERROR);
        }
    }

    @Override
    public Long caseAllCount(String workspaceId) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(projectResponses) ? customizedApiTestCaseRepository.count(projectIds)
                : 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace api case count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_WORKSPACE_API_CASE_COUNT_ERROR);
        }
    }

    @Override
    public Long apiAllCount(String workspaceId) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(projectIds) ? customizedApiRepository.count(projectIds) : 0L;
        } catch (Exception e) {
            log.error("Failed to get the Workspace api count!", e);
            throw ExceptionUtils.mpe(ErrorCode.GET_WORKSPACE_API_COUNT_ERROR);
        }
    }

    @Override
    public List<CaseCountStatisticsResponse> sceneCaseGroupDayCount(String workspaceId, Integer day) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupDay(projectIds, day, SceneCaseEntity.class);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace scene case group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_SCENE_CASE_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public List<CaseCountStatisticsResponse> caseJobGroupDayCount(String workspaceId, Integer day) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupDay(projectIds, day, ApiTestCaseJobEntity.class);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace case job group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_CASE_JOB_GROUP_BY_DAY_ERROR);
        }
    }

    @Override
    public List<CaseCountStatisticsResponse> sceneCaseJobGroupDayCount(String workspaceId, Integer day) {
        try {
            List<ProjectResponse> projectResponses = projectService.list(workspaceId);
            List<String> projectIds = projectResponses.stream().map(ProjectResponse::getId)
                .collect(Collectors.toList());
            return groupDay(projectIds, day, SceneCaseJobEntity.class);
        } catch (ApiTestPlatformException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception e) {
            log.error("Failed to get the Workspace scene case job group by day!", e);
            throw new ApiTestPlatformException(GET_WORKSPACE_SCENE_CASE_JOB_GROUP_BY_DAY_ERROR);
        }
    }

}
