package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.SCENE_CASE_API;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.enums.ApiBindingStatus;
import com.sms.satp.common.enums.ApiType;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.AddSceneCaseApi;
import com.sms.satp.dto.request.AddSceneCaseApiByIdsRequest;
import com.sms.satp.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.entity.api.ApiEntity;
import com.sms.satp.entity.apitestcase.ApiTestCase;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.ApiTestCaseMapper;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.repository.ApiRepository;
import com.sms.satp.repository.ApiTestCaseRepository;
import com.sms.satp.repository.CustomizedSceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseApiServiceImpl implements SceneCaseApiService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final ApiTestCaseRepository apiTestCaseRepository;
    private final SceneCaseRepository sceneCaseRepository;
    private final ApiRepository apiRepository;
    private final ApiTestCaseMapper apiTestCaseMapper;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;

    public SceneCaseApiServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper, ApiTestCaseRepository apiTestCaseRepository,
        SceneCaseRepository sceneCaseRepository, ApiRepository apiRepository,
        ApiTestCaseMapper apiTestCaseMapper,
        CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.apiTestCaseRepository = apiTestCaseRepository;
        this.sceneCaseRepository = sceneCaseRepository;
        this.apiRepository = apiRepository;
        this.apiTestCaseMapper = apiTestCaseMapper;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_API,
        template = "{{#addSceneCaseApiDto.addSceneCaseApiRequestList?.![#this.apiTestCase.apiName]}}",
        projectId = "addSceneCaseApiRequestList[0].projectId")
    public Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto) {
        log.info("SceneCaseApiService-batchAdd()-params: [SceneCaseApi]={}", addSceneCaseApiDto.toString());
        try {
            List<SceneCaseApi> caseApiList =
                sceneCaseApiMapper.toSceneCaseApiListByAddRequest(addSceneCaseApiDto.getAddSceneCaseApiRequestList());
            sceneCaseApiRepository.insert(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE_API,
        template = "{{#result?.![#this.apiTestCase.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("SceneCaseApiService-deleteByIds()-params: [ids]={}", ids);
        try {
            Long count = sceneCaseApiRepository.deleteAllByIdIsIn(ids);
            return count > 0;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseApi!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API,
        template = "{{#updateSceneCaseApiRequest.apiTestCase.apiName}}")
    public Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest) {
        log.info("SceneCaseApiService-edit()-params: [SceneCaseApi]={}", updateSceneCaseApiRequest.toString());
        try {
            SceneCaseApi sceneCaseApi = sceneCaseApiMapper.toSceneCaseApiByUpdateRequest(updateSceneCaseApiRequest);
            sceneCaseApiRepository.save(sceneCaseApi);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API,
        template = "{{#sceneCaseApiList[0].apiTestCase.apiName}}")
    public Boolean editAll(List<SceneCaseApi> sceneCaseApiList) {
        log.info("SceneCaseApiService-edit()-params: [SceneCaseApi]={}", sceneCaseApiList.toString());
        try {
            sceneCaseApiRepository.saveAll(sceneCaseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API,
        template = "{{#updateSceneCaseApiSortOrderDto.sceneCaseApiRequestList?.![#this.apiTestCase.apiName]}}",
        projectId = "sceneCaseApiRequestList[0].projectId")
    public Boolean batchEdit(BatchUpdateSceneCaseApiRequest updateSceneCaseApiSortOrderDto) {
        log.info("SceneCaseApiService-batchEdit()-params: [SceneCaseApi]={}",
            updateSceneCaseApiSortOrderDto.toString());
        try {
            if (!updateSceneCaseApiSortOrderDto.getSceneCaseApiRequestList().isEmpty()) {
                List<SceneCaseApi> caseApiList = sceneCaseApiMapper
                    .toSceneCaseApiList(updateSceneCaseApiSortOrderDto.getSceneCaseApiRequestList());
                sceneCaseApiRepository.saveAll(caseApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to batch edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(BATCH_EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean removed) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).removed(removed).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER.getFiled());
            List<SceneCaseApi> sceneCaseApiList = sceneCaseApiRepository.findAll(example, sort);
            return sceneCaseApiList.stream().map(sceneCaseApiMapper::toSceneCaseApiDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public List<SceneCaseApi> listBySceneCaseId(String sceneCaseId) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).build());
            return sceneCaseApiRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public List<SceneCaseApi> getApiBySceneCaseId(String sceneCaseId, boolean remove) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).removed(remove).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER.getFiled());
            return sceneCaseApiRepository.findAll(example, sort);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public SceneCaseApiResponse getSceneCaseApiById(String id) {
        try {
            Optional<SceneCaseApi> sceneCaseApi = sceneCaseApiRepository.findById(id);
            return sceneCaseApi.map(sceneCaseApiMapper::toSceneCaseApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi by id!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean add(AddSceneCaseApiByIdsRequest request) {
        try {
            Optional<SceneCase> sceneCase = sceneCaseRepository.findById(request.getSceneCaseId());
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            for (AddSceneCaseApi addSceneCaseApi : request.getSceneCaseApis()) {
                if (BooleanUtils.isTrue(addSceneCaseApi.getIsCase())) {
                    addSceneCaseApiByTestCase(sceneCase.get(), addSceneCaseApi);
                } else {
                    addSceneCaseApiByApi(sceneCase.get(), addSceneCaseApi);
                }
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public Boolean updateStatusByApiIds(List<String> ids, ApiBindingStatus apiBindingStatus) {
        try {
            List<SceneCaseApi> sceneCaseApiList = customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
                for (SceneCaseApi sceneCaseApi : sceneCaseApiList) {
                    sceneCaseApi.setApiBindingStatus(apiBindingStatus);
                }
                sceneCaseApiRepository.saveAll(sceneCaseApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to update status the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    private void addSceneCaseApiByApi(SceneCase sceneCase, AddSceneCaseApi addSceneCaseApi) {
        Optional<ApiEntity> apiEntity = apiRepository.findById(addSceneCaseApi.getId());
        if (apiEntity.isPresent()) {
            SceneCaseApi sceneCaseApi =
                SceneCaseApi.builder().apiTestCase(apiTestCaseMapper.toEntityByApiEntity(apiEntity.get()))
                    .sceneCaseId(sceneCase.getId())
                    .projectId(sceneCase.getProjectId()).order(addSceneCaseApi.getOrder()).apiType(ApiType.API)
                    .apiBindingStatus(ApiBindingStatus.BINDING).build();
            sceneCaseApiRepository.insert(sceneCaseApi);
        }
    }

    private void addSceneCaseApiByTestCase(SceneCase sceneCase, AddSceneCaseApi addSceneCaseApi) {
        Optional<ApiTestCase> apiTestCase = apiTestCaseRepository.findById(addSceneCaseApi.getId());
        if (apiTestCase.isPresent()) {
            SceneCaseApi sceneCaseApi =
                SceneCaseApi.builder().apiTestCase(apiTestCase.get()).sceneCaseId(sceneCase.getId())
                    .projectId(sceneCase.getProjectId()).order(addSceneCaseApi.getOrder()).apiType(ApiType.API)
                    .apiBindingStatus(ApiBindingStatus.BINDING).build();
            sceneCaseApiRepository.insert(sceneCaseApi);
        }
    }

}
