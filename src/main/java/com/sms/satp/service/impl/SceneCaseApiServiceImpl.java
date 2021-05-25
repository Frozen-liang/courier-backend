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

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.satp.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.satp.dto.request.UpdateSceneCaseApiRequest;
import com.sms.satp.dto.response.SceneCaseApiResponse;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseApiServiceImpl implements SceneCaseApiService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;

    public SceneCaseApiServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_API, template = "{{#addSceneCaseApiDto"
        + ".addSceneCaseApiRequestList?.![#this.apiName]}}", projectId = "addSceneCaseApiRequestList[0].projectId")
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
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE_API, template = "{{#result?.![#this.apiName]}}",
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
        template = "{{#updateSceneCaseApiRequest.apiName}}")
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
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API, template = "{{#sceneCaseApiList[0].apiName}}")
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
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API, template = "{{#updateSceneCaseApiSortOrderDto"
        + ".sceneCaseApiRequestList?.![#this.apiName]}}", projectId = "sceneCaseApiRequestList[0].projectId")
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
    public List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean remove) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).removed(remove).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER_NUMBER.getFiled());
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
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneFiled.ORDER_NUMBER.getFiled());
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

}
