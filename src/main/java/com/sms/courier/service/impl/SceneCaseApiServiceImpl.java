package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCENE_CASE_API;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;

import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.enums.ApiBindingStatus;
import com.sms.courier.common.field.SceneField;
import com.sms.courier.dto.request.BatchAddSceneCaseApiRequest;
import com.sms.courier.dto.request.BatchUpdateSceneCaseApiRequest;
import com.sms.courier.dto.request.UpdateSceneCaseApiRequest;
import com.sms.courier.dto.response.SceneCaseApiResponse;
import com.sms.courier.entity.scenetest.SceneCaseApiEntity;
import com.sms.courier.mapper.SceneCaseApiMapper;
import com.sms.courier.repository.CustomizedSceneCaseApiRepository;
import com.sms.courier.repository.SceneCaseApiRepository;
import com.sms.courier.service.SceneCaseApiService;
import com.sms.courier.utils.ExceptionUtils;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseApiServiceImpl implements SceneCaseApiService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository;

    public SceneCaseApiServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper, CustomizedSceneCaseApiRepository customizedSceneCaseApiRepository) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.customizedSceneCaseApiRepository = customizedSceneCaseApiRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_API,
        template = "{{#addSceneCaseApiDto.addSceneCaseApiRequestList?.![#this.apiTestCase.apiName]}}",
        projectId = "addSceneCaseApiRequestList[0].projectId")
    public Boolean batchAdd(BatchAddSceneCaseApiRequest addSceneCaseApiDto) {
        log.info("SceneCaseApiService-batchAdd()-params: [SceneCaseApi]={}", addSceneCaseApiDto.toString());
        try {
            List<SceneCaseApiEntity> caseApiList =
                sceneCaseApiMapper.toSceneCaseApiListByAddRequest(addSceneCaseApiDto.getAddSceneCaseApiRequestList());
            sceneCaseApiRepository.insert(caseApiList);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = SCENE_CASE_API,
        template = "{{#result?.![#this.apiTestCase.apiName]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("SceneCaseApiService-deleteByIds()-params: [ids]={}", ids);
        try {
            Long count = sceneCaseApiRepository.deleteAllByIdIsIn(ids);
            return count > 0;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(DELETE_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_API,
        template = "{{#updateSceneCaseApiRequest.apiTestCase.apiName}}")
    public Boolean edit(UpdateSceneCaseApiRequest updateSceneCaseApiRequest) {
        log.info("SceneCaseApiService-edit()-params: [SceneCaseApi]={}", updateSceneCaseApiRequest.toString());
        try {
            SceneCaseApiEntity sceneCaseApi = sceneCaseApiMapper
                .toSceneCaseApiByUpdateRequest(updateSceneCaseApiRequest);
            sceneCaseApiRepository.save(sceneCaseApi);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(EDIT_SCENE_CASE_API_ERROR);
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
                List<SceneCaseApiEntity> caseApiList = sceneCaseApiMapper
                    .toSceneCaseApiList(updateSceneCaseApiSortOrderDto.getSceneCaseApiRequestList());
                sceneCaseApiRepository.saveAll(caseApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to batch edit the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(BATCH_EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiResponse> listBySceneCaseId(String sceneCaseId, boolean removed) {
        try {
            Example<SceneCaseApiEntity> example = Example.of(
                SceneCaseApiEntity.builder().sceneCaseId(sceneCaseId).removed(removed).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneField.ORDER.getName());
            List<SceneCaseApiEntity> sceneCaseApiList = sceneCaseApiRepository.findAll(example, sort);
            return sceneCaseApiList.stream().map(sceneCaseApiMapper::toSceneCaseApiDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiEntity> listBySceneCaseId(String sceneCaseId) {
        try {
            Example<SceneCaseApiEntity> example = Example.of(
                SceneCaseApiEntity.builder().sceneCaseId(sceneCaseId).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneField.ORDER.getName());
            return sceneCaseApiRepository.findAll(example, sort);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiEntity> getApiBySceneCaseId(String sceneCaseId, boolean remove) {
        try {
            Example<SceneCaseApiEntity> example = Example.of(
                SceneCaseApiEntity.builder().sceneCaseId(sceneCaseId).removed(remove).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SceneField.ORDER.getName());
            return sceneCaseApiRepository.findAll(example, sort);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi list by sceneCaseId!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR);
        }
    }

    @Override
    public SceneCaseApiResponse getSceneCaseApiById(String id) {
        try {
            Optional<SceneCaseApiEntity> sceneCaseApi = sceneCaseApiRepository.findById(id);
            return sceneCaseApi.map(sceneCaseApiMapper::toSceneCaseApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi by id!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_API_BY_ID_ERROR);
        }
    }

    @Override
    public Boolean updateStatusByApiIds(List<String> ids, ApiBindingStatus apiBindingStatus) {
        try {
            List<SceneCaseApiEntity> sceneCaseApiList = customizedSceneCaseApiRepository.findSceneCaseApiByApiIds(ids);
            if (CollectionUtils.isNotEmpty(sceneCaseApiList)) {
                for (SceneCaseApiEntity sceneCaseApi : sceneCaseApiList) {
                    sceneCaseApi.getApiTestCase().setStatus(apiBindingStatus);
                }
                sceneCaseApiRepository.saveAll(sceneCaseApiList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to update status the SceneCaseApi!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_API_ERROR);
        }
    }

}
