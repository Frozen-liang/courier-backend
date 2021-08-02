package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.SCENE_CASE;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.RECOVER;
import static com.sms.satp.common.enums.OperationType.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_CONN_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.RECOVER_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_SCENE_CASE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_API_ENTITY_NOT_EXITS_ERROR;
import static com.sms.satp.common.exception.ErrorCode.THE_API_TEST_CASE_NOT_EXITS_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonField;
import com.sms.satp.dto.request.AddCaseTemplateApi;
import com.sms.satp.dto.request.AddCaseTemplateConnRequest;
import com.sms.satp.dto.request.AddSceneCaseApi;
import com.sms.satp.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.satp.dto.request.AddSceneCaseRequest;
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseConnRequest;
import com.sms.satp.dto.request.UpdateSceneCaseRequest;
import com.sms.satp.dto.response.SceneCaseApiConnResponse;
import com.sms.satp.dto.response.SceneCaseResponse;
import com.sms.satp.dto.response.SceneTemplateResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.apitestcase.ApiTestCaseEntity;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateApiEntity;
import com.sms.satp.entity.scenetest.SceneCaseApiEntity;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.SceneCaseService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.SecurityUtil;
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
    private final ApiRepository apiRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final CaseTemplateApiMapper caseTemplateApiMapper;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService,
        CaseTemplateApiService caseTemplateApiService,
        ApiTestCaseRepository apiTestCaseRepository, ApiRepository apiRepository,
        ApiTestCaseMapper apiTestCaseMapper, SceneCaseApiRepository sceneCaseApiRepository,
        CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper, CaseTemplateApiMapper caseTemplateApiMapper) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
        this.caseTemplateApiService = caseTemplateApiService;
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.apiRepository = apiRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
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
            for (String id : ids) {
                sceneCaseRepository.deleteById(id);
                deleteSceneCaseApi(id);
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
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE, template = "{{#updateSceneCaseRequest.name}}")
    public Boolean edit(UpdateSceneCaseRequest updateSceneCaseRequest) {
        log.info("SceneCaseService-edit()-params: [SceneCase]={}", updateSceneCaseRequest.toString());
        try {
            SceneCaseEntity sceneCase = sceneCaseMapper.toUpdateSceneCase(updateSceneCaseRequest);
            sceneCaseRepository.save(sceneCase);
            return Boolean.TRUE;
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
            Optional<SceneCaseEntity> optional = sceneCaseRepository.findById(id);
            SceneCaseResponse dto = sceneCaseMapper.toDto(optional.orElse(null));
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
            if (CollectionUtils.isNotEmpty(updateSceneTemplateRequest.getUpdateSceneCaseApiRequests())) {
                List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList();
                for (UpdateSceneCaseApiConnRequest request :
                    updateSceneTemplateRequest.getUpdateSceneCaseApiRequests()) {
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
    public Boolean addTemplate(AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        try {
            SceneCaseEntity sceneCase =
                sceneCaseRepository.findById(addCaseTemplateConnRequest.getSceneCaseId())
                    .orElseThrow(() -> ExceptionUtils.mpe(GET_SCENE_CASE_BY_ID_ERROR));
            for (AddCaseTemplateApi addCaseTemplateApi : addCaseTemplateConnRequest.getCaseTemplateIds()) {
                List<CaseTemplateApiEntity> caseTemplateApiList =
                    caseTemplateApiService.listByCaseTemplateId(addCaseTemplateApi.getId());
                SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder()
                    .sceneCaseId(addCaseTemplateConnRequest.getSceneCaseId())
                    .caseTemplateId(addCaseTemplateApi.getId())
                    .apiType(ApiType.API)
                    .projectId(sceneCase.getProjectId())
                    .order(addCaseTemplateApi.getOrder())
                    .caseTemplateApiConnList(sceneCaseMapper.toCaseTemplateApiConnList(caseTemplateApiList))
                    .build();
                sceneCaseApiRepository.insert(sceneCaseApi);
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
    public Boolean deleteConn(String sceneCaseApiId) {
        try {
            sceneCaseApiRepository.deleteById(sceneCaseApiId);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase conn!", e);
            throw ExceptionUtils.mpe(DELETE_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE,
        template = "{{#res?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids", queryResultKey = "res"))
    public Boolean delete(List<String> ids) {
        try {
            customizedSceneCaseRepository.deleteByIds(ids);
            List<SceneCaseApiEntity> sceneCaseApiEntityList = customizedSceneCaseApiRepository
                .findSceneCaseApiIdsBySceneCaseIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiEntityList)) {
                List<String> sceneCaseApiIds = sceneCaseApiEntityList.stream().map(SceneCaseApiEntity::getId).collect(
                    Collectors.toList());
                customizedSceneCaseApiRepository.deleteByIds(sceneCaseApiIds);
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
        template = "{{#result?.![#this.caseName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean recover(List<String> ids) {
        try {
            customizedSceneCaseRepository.recover(ids);
            List<SceneCaseApiEntity> sceneCaseApiEntityList = customizedSceneCaseApiRepository
                .findSceneCaseApiIdsBySceneCaseIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiEntityList)) {
                List<String> sceneCaseApiIds = sceneCaseApiEntityList.stream().map(SceneCaseApiEntity::getId).collect(
                    Collectors.toList());
                customizedSceneCaseApiRepository.recover(sceneCaseApiIds);
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

    private void deleteSceneCaseApi(String id) {
        List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(id);
        if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
            List<String> ids = sceneCaseApiList.stream().map(SceneCaseApiEntity::getId).collect(Collectors.toList());
            sceneCaseApiService.deleteByIds(ids);
        }
    }

    private void addSceneCaseApi(SceneCaseEntity sceneCase, AddSceneCaseApi addSceneCaseApi) {
        ApiTestCaseEntity apiTestCase;
        if (BooleanUtils.isTrue(addSceneCaseApi.isCase())) {
            apiTestCase =
                apiTestCaseRepository.findById(addSceneCaseApi.getId())
                    .orElseThrow(() -> ExceptionUtils.mpe(THE_API_TEST_CASE_NOT_EXITS_ERROR));
        } else {
            ApiEntity apiEntity = apiRepository.findById(addSceneCaseApi.getId())
                .orElseThrow(() -> ExceptionUtils.mpe(THE_API_ENTITY_NOT_EXITS_ERROR));
            apiTestCase = apiTestCaseMapper.toEntityByApiEntity(apiEntity);
        }
        apiTestCase.setExecute(Boolean.TRUE);
        SceneCaseApiEntity sceneCaseApi =
            SceneCaseApiEntity.builder()
                .apiTestCase(apiTestCase)
                .sceneCaseId(sceneCase.getId())
                .order(addSceneCaseApi.getOrder())
                .projectId(sceneCase.getProjectId())
                .apiType(ApiType.API)
                .build();
        sceneCaseApiRepository.insert(sceneCaseApi);
    }

    private void resetSceneCaseApiConn(SceneCaseApiConnResponse response,
        SceneCaseApiEntity sceneCaseApi) {
        List<CaseTemplateApiEntity> caseTemplateApiList =
            caseTemplateApiService.listByCaseTemplateId(sceneCaseApi.getCaseTemplateId());
        Map<String, Boolean> isExecuteMap =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isExecute));
        caseTemplateApiList.forEach(api -> api.getApiTestCase().setExecute(
            isExecuteMap.getOrDefault(api.getId(), Boolean.TRUE)));
        response.setCaseTemplateApiList(caseTemplateApiMapper.toCaseTemplateApiDtoList(caseTemplateApiList));
    }

}
