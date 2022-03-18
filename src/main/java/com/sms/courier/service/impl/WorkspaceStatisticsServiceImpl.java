package com.sms.courier.service.impl;

import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_DAY_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_CASE_GROUP_BY_USER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_COUNT_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_JOB_GROUP_BY_USER_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_WORKSPACE_PROJECT_CASE_PERCENTAGE_ERROR;

import com.sms.courier.common.enums.StatisticsCountType;
import com.sms.courier.common.enums.StatisticsGroupQueryType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.response.CaseCountUserStatisticsResponse;
import com.sms.courier.dto.response.CountStatisticsResponse;
import com.sms.courier.dto.response.ProjectResponse;
import com.sms.courier.dto.response.WorkspaceProjectCaseStatisticsResponse;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.job.ApiTestCaseJobEntity;
import com.sms.courier.entity.job.SceneCaseJobEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.repository.CommonStatisticsRepository;
import com.sms.courier.repository.CustomizedApiRepository;
import com.sms.courier.repository.CustomizedApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.service.ProjectService;
import com.sms.courier.service.ProjectStatisticsService;
import com.sms.courier.service.WorkspaceStatisticsService;
import com.sms.courier.utils.NumberUtil;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WorkspaceStatisticsServiceImpl extends AbstractStatisticsService implements WorkspaceStatisticsService {

    private final ProjectService projectService;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final CustomizedApiTestCaseRepository customizedApiTestCaseRepository;
    private final CustomizedApiRepository customizedApiRepository;
    private final ProjectStatisticsService projectStatisticsService;

    private final Map<String, Function<List<String>, Long>> allCountTypeMap = new HashMap<>();
    private final Map<String, Class<?>> groupQueryTypeMap = new HashMap<>();
    private final Map<String, Function<ObjectId, Long>> percentageTypeMap = new HashMap<>();


    public WorkspaceStatisticsServiceImpl(ProjectService projectService,
        CommonStatisticsRepository commonStatisticsRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        CustomizedApiTestCaseRepository customizedApiTestCaseRepository,
        CustomizedApiRepository customizedApiRepository,
        ProjectStatisticsService projectStatisticsService) {
        super(commonStatisticsRepository);
        this.projectService = projectService;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.customizedApiTestCaseRepository = customizedApiTestCaseRepository;
        this.customizedApiRepository = customizedApiRepository;
        this.projectStatisticsService = projectStatisticsService;
    }

    @PostConstruct
    public void allCountTypeMapInit() {
        allCountTypeMap.put(StatisticsCountType.API.getName(), customizedApiRepository::count);
        allCountTypeMap.put(StatisticsCountType.API_TEST_CASE.getName(), customizedApiTestCaseRepository::count);
        allCountTypeMap.put(StatisticsCountType.SCENE_CASE.getName(), customizedSceneCaseRepository::count);

        groupQueryTypeMap.put(StatisticsGroupQueryType.API_TEST_CASE.getName(), ApiTestCaseEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.SCENE_CASE.getName(), SceneCaseEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.API_TEST_CASE_JOB.getName(), ApiTestCaseJobEntity.class);
        groupQueryTypeMap.put(StatisticsGroupQueryType.SCENE_CASE_JOB.getName(), SceneCaseJobEntity.class);

        percentageTypeMap.put(StatisticsCountType.API_TEST_CASE.getName(), projectStatisticsService::caseCount);
        percentageTypeMap.put(StatisticsCountType.SCENE_CASE.getName(), projectStatisticsService::sceneCount);

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
    public List<CountStatisticsResponse> groupDayCount(String workspaceId, Integer day, String groupType) {
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
                    percentageTypeMap.get(queryType).apply(new ObjectId(project.getId())).intValue();
                int apiCountInt = projectStatisticsService.apiAllCount(project.getId()).intValue();
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
