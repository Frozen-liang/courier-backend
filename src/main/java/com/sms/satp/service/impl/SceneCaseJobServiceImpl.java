package com.sms.satp.service.impl;

import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_PROJECT_ENVIRONMENT_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_JOB_PAGE_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.PageDto;
import com.sms.satp.dto.request.AddSceneCaseJobRequest;
import com.sms.satp.entity.datacollection.TestData;
import com.sms.satp.entity.env.ProjectEnvironment;
import com.sms.satp.entity.job.JobSceneCaseApi;
import com.sms.satp.entity.job.SceneCaseJob;
import com.sms.satp.entity.job.common.JobDataCollection;
import com.sms.satp.entity.job.common.JobEnvironment;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.DataCollectionMapper;
import com.sms.satp.mapper.ProjectEnvironmentMapper;
import com.sms.satp.mapper.SceneCaseJobMapper;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CustomizedCaseTemplateApiRepository;
import com.sms.satp.repository.CustomizedSceneCaseJobRepository;
import com.sms.satp.repository.ProjectEnvironmentRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseJobRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.SceneCaseJobService;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseJobServiceImpl implements SceneCaseJobService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final ProjectEnvironmentRepository projectEnvironmentRepository;
    private final SceneCaseRepository sceneCaseRepository;
    private final CaseTemplateConnRepository caseTemplateConnRepository;
    private final SceneCaseJobRepository sceneCaseJobRepository;
    private final DataCollectionMapper dataCollectionMapper;
    private final CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository;
    private final SceneCaseJobMapper sceneCaseJobMapper;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;
    private final CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository;

    public SceneCaseJobServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        ProjectEnvironmentRepository projectEnvironmentRepository,
        SceneCaseRepository sceneCaseRepository,
        CaseTemplateConnRepository caseTemplateConnRepository,
        SceneCaseJobRepository sceneCaseJobRepository,
        DataCollectionMapper dataCollectionMapper,
        CustomizedSceneCaseJobRepository customizedSceneCaseJobRepository,
        SceneCaseJobMapper sceneCaseJobMapper, ProjectEnvironmentMapper projectEnvironmentMapper,
        CustomizedCaseTemplateApiRepository customizedCaseTemplateApiRepository) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.projectEnvironmentRepository = projectEnvironmentRepository;
        this.sceneCaseRepository = sceneCaseRepository;
        this.caseTemplateConnRepository = caseTemplateConnRepository;
        this.sceneCaseJobRepository = sceneCaseJobRepository;
        this.dataCollectionMapper = dataCollectionMapper;
        this.customizedSceneCaseJobRepository = customizedSceneCaseJobRepository;
        this.sceneCaseJobMapper = sceneCaseJobMapper;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
        this.customizedCaseTemplateApiRepository = customizedCaseTemplateApiRepository;
    }

    @Override
    public Boolean add(AddSceneCaseJobRequest request) {
        try {
            Optional<ProjectEnvironment> environment =
                projectEnvironmentRepository.findById(request.getEnvironmentId());
            if (environment.isEmpty()) {
                throw new ApiTestPlatformException(GET_PROJECT_ENVIRONMENT_BY_ID_ERROR);
            }
            JobEnvironment jobEnvironment = projectEnvironmentMapper.toJobEnvironment(environment.get());
            Optional<SceneCase> sceneCase = sceneCaseRepository.findById(request.getSceneCaseId());
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            List<JobSceneCaseApi> caseList = getApiCaseList(request);

            if (Objects.isNull(request.getDataCollection())) {
                SceneCaseJob sceneCaseJob = SceneCaseJob.builder().environment(jobEnvironment).caseList(caseList)
                    .sceneCaseId(request.getSceneCaseId()).projectId(request.getProjectId()).build();
                sceneCaseJobRepository.insert(sceneCaseJob);
            } else {
                for (TestData testData : request.getDataCollection().getDataList()) {
                    JobDataCollection jobDataCollection =
                        dataCollectionMapper.toDataCollectionJob(request.getDataCollection(), testData);
                    SceneCaseJob sceneCaseJob = SceneCaseJob.builder()
                        .sceneCaseId(request.getSceneCaseId())
                        .environment(jobEnvironment)
                        .dataCollection(jobDataCollection)
                        .caseList(caseList)
                        .projectId(request.getProjectId())
                        .build();
                    sceneCaseJobRepository.insert(sceneCaseJob);
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseJob!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_JOB_ERROR);
        }
    }

    @Override
    public Page<SceneCaseJob> page(String sceneCaseId, List<String> userIds, PageDto pageDto) {
        try {
            return customizedSceneCaseJobRepository.page(sceneCaseId, userIds, pageDto);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseJob page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_JOB_PAGE_ERROR);
        }
    }

    @Override
    public SceneCaseJob get(String jobId) {
        try {
            SceneCaseJob job = SceneCaseJob.builder().id(jobId).build();
            Example<SceneCaseJob> example = Example.of(job);
            Optional<SceneCaseJob> sceneCaseJob = sceneCaseJobRepository.findOne(example);
            return sceneCaseJob.orElseGet(null);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseJob page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_JOB_ERROR);
        }
    }

    @Override
    public Boolean edit(SceneCaseJob sceneCaseJob) {
        try {
            sceneCaseJobRepository.save(sceneCaseJob);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseJob!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_JOB_ERROR);
        }
    }

    private List<JobSceneCaseApi> getApiCaseList(AddSceneCaseJobRequest request) {
        List<JobSceneCaseApi> caseList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(request.getSceneCaseApiIds())) {
            List<SceneCaseApi> sceneCaseApiList = Lists.newArrayList();
            sceneCaseApiRepository.findAllById(request.getSceneCaseApiIds()).forEach(sceneCaseApiList::add);
            caseList.addAll(sceneCaseJobMapper.toJobSceneCaseApiList(sceneCaseApiList));
        }

        if (CollectionUtils.isNotEmpty(request.getCaseTemplateConnIds())) {
            List<CaseTemplateConn> caseTemplateConn = Lists.newArrayList();
            caseTemplateConnRepository.findAllById(request.getCaseTemplateConnIds()).forEach(caseTemplateConn::add);
            List<CaseTemplateApi> caseTemplateApiList = customizedCaseTemplateApiRepository.findByCaseTemplateIds(
                caseTemplateConn.stream().map(CaseTemplateConn::getCaseTemplateId).collect(Collectors.toList()));
            Map<String, Integer> apiConn = caseTemplateConn.stream()
                .map(CaseTemplateConn::getCaseTemplateApiConnList)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::getOrder));
            resetOrder(caseTemplateApiList, apiConn);
            caseList.addAll(sceneCaseJobMapper.toJobSceneCaseApiListByTemplate(caseTemplateApiList));
        }

        if (CollectionUtils.isNotEmpty(caseList)) {
            caseList.sort(Comparator.comparingInt(JobSceneCaseApi::getOrder));
        }
        return caseList;
    }

    private void resetOrder(List<CaseTemplateApi> caseTemplateApiList,
        Map<String, Integer> apiConn) {
        for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
            caseTemplateApi.setOrder(apiConn.get(caseTemplateApi.getId()));
        }
    }

}
