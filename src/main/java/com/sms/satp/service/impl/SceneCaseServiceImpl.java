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

import com.google.common.collect.Lists;
import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonFiled;
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
            //query user by "createUserId",write for filed createUserName.
            sceneCaseRepository.insert(sceneCase);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCase!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_ERROR);
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
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR);
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
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseResponse> page(SearchSceneCaseRequest searchDto, ObjectId projectId) {
        try {
            return customizedSceneCaseRepository.search(searchDto, projectId);
        } catch (Exception e) {
            log.error("Failed to search the SceneCase!", e);
            throw new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR);
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
        } catch (Exception e) {
            log.error("Failed to get the SceneCase conn!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public Boolean editConn(UpdateSceneCaseConnRequest updateSceneTemplateRequest) {
        log.info("SceneCaseService-editConn()-params: [SceneTemplateDto]={}", updateSceneTemplateRequest.toString());
        try {
            Optional<SceneCaseEntity> sceneCase = sceneCaseRepository
                .findById(updateSceneTemplateRequest.getSceneCaseId());
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            if (CollectionUtils.isNotEmpty(updateSceneTemplateRequest.getUpdateSceneCaseApiConnRequest())) {
                List<SceneCaseApiEntity> sceneCaseApiList = Lists.newArrayList();
                for (UpdateSceneCaseApiConnRequest request :
                    updateSceneTemplateRequest.getUpdateSceneCaseApiConnRequest()) {
                    Optional<SceneCaseApiEntity> sceneCaseApi = sceneCaseApiRepository.findById(request.getId());
                    sceneCaseApi.ifPresent(api -> {
                        api.setOrder(request.getOrder());
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
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase conn!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_CONN_ERROR);
        }
    }

    @Override
    public List<SceneCaseEntity> get(String groupId, String projectId) {
        try {
            SceneCaseEntity sceneCase = SceneCaseEntity.builder().groupId(groupId).projectId(projectId).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.GROUP_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<SceneCaseEntity> example = Example.of(sceneCase, exampleMatcher);
            return sceneCaseRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the SceneCase!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Boolean addApi(AddSceneCaseApiByIdsRequest request) {
        try {
            Optional<SceneCaseEntity> sceneCase = sceneCaseRepository.findById(request.getSceneCaseId());
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            int index = customizedSceneCaseApiRepository.findCurrentOrderBySceneCaseId(request.getSceneCaseId());
            for (AddSceneCaseApi addSceneCaseApi : request.getSceneCaseApis()) {
                if (BooleanUtils.isTrue(addSceneCaseApi.getIsCase())) {
                    addSceneCaseApiByTestCase(sceneCase.get(), addSceneCaseApi, index);
                } else {
                    addSceneCaseApiByApi(sceneCase.get(), addSceneCaseApi, index);
                }
                index++;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public Boolean addTemplate(AddCaseTemplateConnRequest addCaseTemplateConnRequest) {
        try {
            Optional<SceneCaseEntity> sceneCase = sceneCaseRepository
                .findById(addCaseTemplateConnRequest.getSceneCaseId());
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            int index = customizedSceneCaseApiRepository
                .findCurrentOrderBySceneCaseId(addCaseTemplateConnRequest.getSceneCaseId());
            for (String caseTemplateId : addCaseTemplateConnRequest.getCaseTemplateIds()) {
                List<CaseTemplateApiEntity> caseTemplateApiList =
                    caseTemplateApiService.listByCaseTemplateId(caseTemplateId);
                SceneCaseApiEntity sceneCaseApi = SceneCaseApiEntity.builder()
                    .sceneCaseId(addCaseTemplateConnRequest.getSceneCaseId())
                    .caseTemplateId(caseTemplateId).apiType(ApiType.API)
                    .projectId(sceneCase.get().getProjectId()).order(index)
                    .caseTemplateApiConnList(sceneCaseMapper.toCaseTemplateApiConnList(caseTemplateApiList))
                    .build();
                sceneCaseApiRepository.insert(sceneCaseApi);
                index++;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi by template!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public Boolean deleteConn(String sceneCaseApiId) {
        try {
            sceneCaseApiRepository.deleteById(sceneCaseApiId);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase conn!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_CONN_ERROR);
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
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR);
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
        } catch (Exception e) {
            log.error("Failed to recover the SceneCase!", e);
            throw new ApiTestPlatformException(RECOVER_SCENE_CASE_ERROR);
        }
    }

    private void deleteSceneCaseApi(String id) {
        List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(id);
        if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
            List<String> ids = sceneCaseApiList.stream().map(SceneCaseApiEntity::getId).collect(Collectors.toList());
            sceneCaseApiService.deleteByIds(ids);
        }
    }

    private void addSceneCaseApiByApi(SceneCaseEntity sceneCase, AddSceneCaseApi addSceneCaseApi, int index) {
        Optional<ApiEntity> apiEntity = apiRepository.findById(addSceneCaseApi.getId());
        if (apiEntity.isPresent()) {
            ApiTestCaseEntity apiTestCase = apiTestCaseMapper.toEntityByApiEntity(apiEntity.get());
            apiTestCase.setExecute(Boolean.TRUE);
            SceneCaseApiEntity sceneCaseApi =
                SceneCaseApiEntity.builder()
                    .apiTestCase(apiTestCase)
                    .sceneCaseId(sceneCase.getId())
                    .order(index)
                    .projectId(sceneCase.getProjectId())
                    .apiType(ApiType.API)
                    .build();
            sceneCaseApiRepository.insert(sceneCaseApi);
        }
    }

    private void addSceneCaseApiByTestCase(SceneCaseEntity sceneCase, AddSceneCaseApi addSceneCaseApi, int index) {
        Optional<ApiTestCaseEntity> apiTestCase = apiTestCaseRepository.findById(addSceneCaseApi.getId());
        if (apiTestCase.isPresent()) {
            ApiTestCaseEntity testCase = apiTestCase.get();
            testCase.setExecute(Boolean.TRUE);
            SceneCaseApiEntity sceneCaseApi =
                SceneCaseApiEntity.builder()
                    .apiTestCase(testCase)
                    .sceneCaseId(sceneCase.getId())
                    .order(index)
                    .projectId(sceneCase.getProjectId())
                    .apiType(ApiType.API)
                    .build();
            sceneCaseApiRepository.insert(sceneCaseApi);
        }
    }

    private void resetSceneCaseApiConn(SceneCaseApiConnResponse response,
        SceneCaseApiEntity sceneCaseApi) {
        List<CaseTemplateApiEntity> caseTemplateApiList =
            caseTemplateApiService.listByCaseTemplateId(sceneCaseApi.getCaseTemplateId());
        Map<String, Boolean> isExecute =
            sceneCaseApi.getCaseTemplateApiConnList().stream().collect(
                Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::isExecute));
        caseTemplateApiList.forEach(api -> api.getApiTestCase().setExecute(isExecute.get(api.getId())));
        response.setCaseTemplateApiList(caseTemplateApiMapper.toCaseTemplateApiDtoList(caseTemplateApiList));
    }
}
