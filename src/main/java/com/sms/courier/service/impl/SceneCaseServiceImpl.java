package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCENE_CASE;
import static com.sms.courier.common.enums.OperationModule.SCENE_CASE_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.RECOVER;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_CONN_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.ENV_CANNOT_REPEATED;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_BY_API_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.RECOVER_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;
import static com.sms.courier.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;

import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.enums.ApiType;
import com.sms.courier.common.enums.ResponseParamsExtractionType;
import com.sms.courier.common.enums.ResultVerificationType;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.field.CommonField;
import com.sms.courier.dto.request.AddCaseTemplateApi;
import com.sms.courier.dto.request.AddCaseTemplateConnRequest;
import com.sms.courier.dto.request.AddSceneCaseApi;
import com.sms.courier.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.courier.dto.request.AddSceneCaseRequest;
import com.sms.courier.dto.request.ApiRequest;
import com.sms.courier.dto.request.EnvDataCollConnRequest;
import com.sms.courier.dto.request.SearchSceneCaseRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseConnRequest;
import com.sms.courier.dto.request.UpdateSceneCaseRequest;
import com.sms.courier.dto.response.DataCollectionResponse;
import com.sms.courier.dto.response.EnvDataCollConnResponse;
import com.sms.courier.dto.response.EnvDataCollResponse;
import com.sms.courier.dto.response.ProjectEnvironmentResponse;
import com.sms.courier.dto.response.SceneCaseApiConnResponse;
import com.sms.courier.dto.response.SceneCaseConnResponse;
import com.sms.courier.dto.response.SceneCaseResponse;
import com.sms.courier.dto.response.SceneTemplateResponse;
import com.sms.courier.entity.api.common.HttpStatusVerification;
import com.sms.courier.entity.api.common.ResponseResultVerification;
import com.sms.courier.entity.apitestcase.ApiTestCaseEntity;
import com.sms.courier.entity.scenetest.CaseTemplateApiConn;
import com.sms.courier.entity.scenetest.CaseTemplateApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.mapper.ApiTestCaseMapper;
import com.sms.courier.mapper.CaseTemplateApiMapper;
import com.sms.courier.mapper.DataCollectionMapper;
import com.sms.courier.mapper.MatchParamInfoMapper;
import com.sms.courier.mapper.ProjectEnvironmentMapper;
import com.sms.courier.mapper.SceneCaseApiMapper;
import com.sms.courier.mapper.SceneCaseMapper;
import com.sms.courier.repository.ApiTestCaseRepository;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseRepository;
import com.sms.courier.service.CaseApiCountHandler;
import com.sms.courier.service.CaseTemplateApiService;
import com.sms.courier.service.DataCollectionService;
import com.sms.courier.service.ProjectEnvironmentService;
import com.sms.courier.service.SceneCaseApiService;
import com.sms.courier.service.SceneCaseService;
import com.sms.courier.service.ScheduleService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseServiceImpl implements SceneCaseService {

    private final SceneCaseRepository sceneCaseRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final SceneCaseMapper sceneCaseMapper;
    private final SceneCaseApiService sceneCaseApiService;
    private final CaseTemplateApiService caseTemplateApiService;
    private final ApiTestCaseRepository apiTestCaseRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final CaseTemplateApiMapper caseTemplateApiMapper;
    private final CaseApiCountHandler caseApiCountHandler;
    private final MatchParamInfoMapper matchParamInfoMapper;
    private final ScheduleService scheduleService;
    private final ProjectEnvironmentService projectEnvironmentService;
    private final ProjectEnvironmentMapper projectEnvironmentMapper;
    private final DataCollectionService dataCollectionService;
    private final DataCollectionMapper dataCollectionMapper;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService,
        CaseTemplateApiService caseTemplateApiService,
        ApiTestCaseRepository apiTestCaseRepository,
        ApiTestCaseMapper apiTestCaseMapper, SceneCaseApiRepository sceneCaseApiRepository,
        CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper, CaseTemplateApiMapper caseTemplateApiMapper,
        CaseApiCountHandler caseApiCountHandler, MatchParamInfoMapper matchParamInfoMapper,
        ScheduleService scheduleService, ProjectEnvironmentService projectEnvironmentService,
        ProjectEnvironmentMapper projectEnvironmentMapper,
        DataCollectionService dataCollectionService, DataCollectionMapper dataCollectionMapper) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
        this.caseTemplateApiService = caseTemplateApiService;
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
        this.caseApiCountHandler = caseApiCountHandler;
        this.matchParamInfoMapper = matchParamInfoMapper;
        this.scheduleService = scheduleService;
        this.projectEnvironmentService = projectEnvironmentService;
        this.projectEnvironmentMapper = projectEnvironmentMapper;
        this.dataCollectionService = dataCollectionService;
        this.dataCollectionMapper = dataCollectionMapper;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE, template = "{{#addSceneCaseRequest.name}}")
    public Boolean add(AddSceneCaseRequest addSceneCaseRequest) {
        log.info("SceneCaseService-add()-params: [SceneCase]={}", addSceneCaseRequest.toString());
        try {
            SceneCaseEntity sceneCase = sceneCaseMapper.toAddSceneCase(addSceneCaseRequest);
            sceneCaseRepository.insert(sceneCase);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCase!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = SCENE_CASE, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("SceneCaseService-deleteById()-params: [ids]={}", ids.toString());
        try {
            sceneCaseRepository.deleteAllByIdIsIn(ids);
            sceneCaseApiService.deleteAllBySceneCaseIds(ids);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw ExceptionUtils.mpe(DELETE_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE, template = "{{#updateSceneCaseRequest.name}}")
    public Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest) {
        log.info("SceneCaseService-edit()-params: [SceneCase]={}", updateSceneCaseRequest.toString());
        try {
            SceneCaseEntity sceneCase = sceneCaseMapper.toUpdateSceneCase(updateSceneCaseRequest);
            try {
                if (CollectionUtils.isNotEmpty(updateSceneCaseRequest.getEnvDataCollConnList())) {
                    updateSceneCaseRequest.getEnvDataCollConnList().stream().collect(
                        Collectors.toMap(EnvDataCollConnRequest::getEnvId, EnvDataCollConnRequest::getDataCollId));
                }
            } catch (Exception e) {
                log.error("The environment cannot be repeated!", e);
                throw ExceptionUtils.mpe(ENV_CANNOT_REPEATED);
            }
            sceneCaseRepository.save(sceneCase);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw ExceptionUtils.mpe(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseResponse> page(SearchSceneCaseRequest searchDto, ObjectId projectId) {
        try {
            return customizedSceneCaseRepository.search(searchDto, projectId);
        } catch (Exception e) {
            log.error("Failed to search the SceneCase!", e);
            throw ExceptionUtils.mpe(SEARCH_SCENE_CASE_ERROR);
        }
    }

    @Override
    public SceneTemplateResponse getConn(String id) {
        try {
            SceneCaseResponse sceneCaseResponse = customizedSceneCaseRepository.findById(id).orElse(null);
            SceneCaseConnResponse dto = setSceneCaseConnDto(sceneCaseResponse);
            List<SceneCaseApiConnResponse> responsesList = Lists.newArrayList();
            List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(id);
            for (SceneCaseApiEntity sceneCaseApi : sceneCaseApiList) {
                SceneCaseApiConnResponse response = sceneCaseApiMapper.toSceneCaseApiConnResponse(sceneCaseApi);
                if (Objects.nonNull(sceneCaseApi.getCaseTemplateId())) {
                    resetSceneCaseApiConn(response, sceneCaseApi);
                }
                responsesList.add(response);
            }
            return SceneTemplateResponse.builder().sceneCaseDto(dto).sceneCaseApiDtoList(responsesList).build();
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to get the SceneCase conn!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public Boolean editConn(UpdateSceneCaseConnRequest updateSceneTemplateRequest) {
        log.info("SceneCaseService-editConn()-params: [SceneTemplateDto]={}", updateSceneTemplateRequest.toString());
        try {
            sceneCaseRepository.findById(updateSceneTemplateRequest.getSceneCaseId())
                .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            if (CollectionUtils.isNotEmpty(updateSceneTemplateRequest.getSceneCaseApiRequest())) {
                List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList();
                for (UpdateSceneCaseApiConnRequest request :
                    updateSceneTemplateRequest.getSceneCaseApiRequest()) {
                    Optional<SceneCaseApiEntity> sceneCaseApi = sceneCaseApiRepository.findById(request.getId());
                    sceneCaseApi.ifPresent(api -> {
                        api.setOrder(request.getOrder());
                        api.setLock(request.isLock());
                        if (Objects.isNull(api.getCaseTemplateId())) {
                            api.getApiTestCase().setExecute(request.getApiTestCase().isExecute());
                        } else {
                            api.setCaseTemplateApiConnList(sceneCaseMapper
                                .toCaseTemplateApiConnListByResponse(request.getCaseTemplateApiList()));
                        }
                        sceneCaseApiList.add(api);
                    });
                }
                sceneCaseApiRepository.saveAll(sceneCaseApiList);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase conn!", e);
            throw ExceptionUtils.mpe(EDIT_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public List<SceneCaseEntity> get(String groupId, String projectId) {
        try {
            SceneCaseEntity sceneCase = SceneCaseEntity.builder().groupId(groupId).projectId(projectId).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonField.PROJECT_ID.getName(), GenericPropertyMatchers.exact())
                .withMatcher(CommonField.GROUP_ID.getName(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<SceneCaseEntity> example = Example.of(sceneCase, exampleMatcher);
            return sceneCaseRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the SceneCase!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_API,
        template = "{{#request.sceneCaseApis?.![#this.name]}}")
    public Boolean addApi(AddSceneCaseApiByIdsRequest request) {
        try {
            SceneCaseEntity sceneCase =
                sceneCaseRepository.findById(request.getSceneCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            for (AddSceneCaseApi addSceneCaseApiRequest : request.getSceneCaseApis()) {
                addSceneCaseApi(sceneCase, addSceneCaseApiRequest);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_API,
        template = "{{#addCaseTemplateConnRequest.caseTemplateIds?.![#this.name]}}")
    public Boolean addTemplate(AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        try {
            SceneCaseEntity sceneCase =
                sceneCaseRepository.findById(addCaseTemplateConnRequest.getSceneCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            for (AddCaseTemplateApi addCaseTemplateApi : addCaseTemplateConnRequest.getCaseTemplateIds()) {
                List<CaseTemplateApiEntity> caseTemplateApiList =
                    caseTemplateApiService.listByCaseTemplateId(addCaseTemplateApi.getId(), Boolean.FALSE);
                SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder()
                    .sceneCaseId(addCaseTemplateConnRequest.getSceneCaseId())
                    .caseTemplateId(addCaseTemplateApi.getId())
                    .apiType(ApiType.API)
                    .projectId(sceneCase.getProjectId())
                    .order(addCaseTemplateApi.getOrder())
                    .caseTemplateApiConnList(sceneCaseMapper.toCaseTemplateApiConnList(caseTemplateApiList))
                    .build();
                sceneCaseApiRepository.insert(sceneCaseApi);
                List<String> apiIds = caseTemplateApiList.stream()
                    .filter(template -> Objects.equals(template.getApiType(), ApiType.API)
                        && Objects.equals(sceneCaseApi.getProjectId(), template.getApiTestCase().getProjectId()))
                    .map(template -> template.getApiTestCase().getApiEntity().getId())
                    .collect(Collectors.toList());
                caseApiCountHandler.addSceneCaseByApiIds(apiIds, Boolean.TRUE);
                List<String> otherObjectApiIds = caseTemplateApiList.stream()
                    .filter(template -> Objects.equals(template.getApiType(), ApiType.API)
                        && !Objects.equals(sceneCaseApi.getProjectId(), template.getApiTestCase().getProjectId()))
                    .map(template -> template.getApiTestCase().getApiEntity().getId())
                    .collect(Collectors.toList());
                caseApiCountHandler.addSceneCaseByApiIds(otherObjectApiIds, Boolean.FALSE);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi by template!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE,
        template = "{{#res?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids", queryResultKey = "res"))
    public Boolean delete(List<String> ids) {
        try {
            customizedSceneCaseRepository.deleteByIds(ids);
            List<String> sceneCaseApiIds = customizedSceneCaseApiRepository
                .findSceneCaseApiIdsBySceneCaseIds(ids);
            scheduleService.removeCaseIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiIds)) {
                customizedSceneCaseApiRepository.deleteByIds(sceneCaseApiIds);
                caseApiCountHandler.deleteSceneCaseBySceneCaseApiIds(sceneCaseApiIds);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw ExceptionUtils.mpe(DELETE_SCENE_CASE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = RECOVER, operationModule = SCENE_CASE,
        template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        try {
            customizedSceneCaseRepository.recover(ids);
            List<String> sceneCaseApiIds = customizedSceneCaseApiRepository.findSceneCaseApiIdsBySceneCaseIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiIds)) {
                customizedSceneCaseApiRepository.recover(sceneCaseApiIds);
                caseApiCountHandler.addSceneCaseBySceneCaseApiIds(sceneCaseApiIds);
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to recover the SceneCase!", e);
            throw ExceptionUtils.mpe(RECOVER_SCENE_CASE_ERROR);
        }
    }

    @Override
    public List<SceneCaseResponse> getByApiId(String apiId) {
        try {
            List<String> sceneCaseId =
                customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(Lists.newArrayList(apiId)).stream()
                    .map(SceneCaseApiEntity::getSceneCaseId)
                    .distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sceneCaseId)) {
                return sceneCaseRepository.findByIdIn(sceneCaseId).stream()
                    .filter(sceneCase -> Objects.equals(Boolean.FALSE, sceneCase.isRemoved()))
                    .map(sceneCaseMapper::toDto).collect(
                        Collectors.toList());
            }
            return Lists.newArrayList();
        } catch (Exception e) {
            log.error("Failed to get the SceneCase by apiId!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_BY_API_ID_ERROR);
        }
    }

    private SceneCaseConnResponse setSceneCaseConnDto(SceneCaseResponse sceneCaseResponse) {
        SceneCaseConnResponse dto = sceneCaseMapper.toConnResponse(sceneCaseResponse);
        List<EnvDataCollConnResponse> connResponses = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(sceneCaseResponse.getEnvDataCollConnList())) {
            for (EnvDataCollResponse response : sceneCaseResponse.getEnvDataCollConnList()) {
                ProjectEnvironmentResponse envResponse = Objects.nonNull(response.getEnvId())
                    ? projectEnvironmentMapper.toDto(projectEnvironmentService.findOne(response.getEnvId())) : null;
                DataCollectionResponse dataResponse = Objects.nonNull(response.getDataCollId())
                    ? dataCollectionMapper.toDto(dataCollectionService.findOne(response.getDataCollId())) : null;
                EnvDataCollConnResponse collConnResponse = EnvDataCollConnResponse.builder()
                    .environment(envResponse)
                    .dataCollection(dataResponse)
                    .build();
                connResponses.add(collConnResponse);
            }
        }
        dto.setEnvDataCollConnList(connResponses);
        return dto;
    }

    private void addSceneCaseApi(SceneCaseEntity sceneCase, AddSceneCaseApi addSceneCaseApi) {
        ApiTestCaseEntity apiTestCase;
        if (BooleanUtils.isTrue(addSceneCaseApi.isCase())) {
            apiTestCase =
                apiTestCaseRepository.findById(addSceneCaseApi.getId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_API_TEST_CASE_NOT_EXITS_ERROR));
        } else {
            apiTestCase = apiTestCaseMapper.toEntityByApiEntity(addSceneCaseApi.getApiEntity());
            resetApiTestCaseByApi(apiTestCase, addSceneCaseApi.getApiEntity());
        }
        apiTestCase.setResponseParamsExtractionType(ResponseParamsExtractionType.JSON);
        apiTestCase.setStatus(ApiBindingStatus.BINDING);
        SceneCaseApiEntity sceneCaseApi =
            SceneCaseApiEntity.builder()
                .apiTestCase(apiTestCase)
                .sceneCaseId(sceneCase.getId())
                .order(addSceneCaseApi.getOrder())
                .projectId(sceneCase.getProjectId())
                .apiType(ApiType.API)
                .build();
        sceneCaseApi = sceneCaseApiRepository.insert(sceneCaseApi);
        if (Objects.nonNull(sceneCaseApi.getId())) {
            if (Objects.equals(sceneCaseApi.getProjectId(), sceneCaseApi.getApiTestCase().getProjectId())) {
                caseApiCountHandler.addSceneCaseByApiIds(List.of(apiTestCase.getApiEntity().getId()), Boolean.TRUE);
            }
            if (!Objects.equals(sceneCaseApi.getProjectId(), sceneCaseApi.getApiTestCase().getProjectId())) {
                caseApiCountHandler.addSceneCaseByApiIds(List.of(apiTestCase.getApiEntity().getId()), Boolean.FALSE);
            }
        }
    }

    private void resetApiTestCaseByApi(ApiTestCaseEntity apiTestCase, ApiRequest apiEntity) {
        apiTestCase.setHttpStatusVerification(HttpStatusVerification.builder().statusCode(
            Constants.HTTP_DEFAULT_STATUS_CODE).build());
        apiTestCase.setResponseResultVerification(ResponseResultVerification.builder()
            .resultVerificationType(ResultVerificationType.JSON)
            .apiResponseJsonType(apiEntity.getApiResponseJsonType())
            .params(matchParamInfoMapper.toMatchParamInfoList(apiEntity.getResponseParams()))
            .build());
    }

    private void resetSceneCaseApiConn(SceneCaseApiConnResponse response,
        SceneCaseApiEntity sceneCaseApi) {
        List<CaseTemplateApiEntity> caseTemplateApiList =
            caseTemplateApiService.listByCaseTemplateId(sceneCaseApi.getCaseTemplateId(), Boolean.FALSE);
        Map<String, Boolean> isExecuteMap =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isExecute));
        Map<String, Boolean> isLockMap =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isLock));
        caseTemplateApiList.forEach(api -> {
            api.getApiTestCase().setExecute(isExecuteMap.getOrDefault(api.getId(), Boolean.TRUE));
            api.setLock(isLockMap.getOrDefault(api.getId(), Boolean.FALSE));
        });
        response.setCaseTemplateApiList(caseTemplateApiMapper.toCaseTemplateApiDtoList(caseTemplateApiList));
    }

}
