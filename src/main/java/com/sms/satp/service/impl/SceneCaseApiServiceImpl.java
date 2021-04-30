package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.SearchFiled;
import com.sms.satp.common.enums.OperationType;
import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiLogDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.SceneCaseApiLogMapper;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.event.entity.SceneCaseApiLogEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseApiServiceImpl implements SceneCaseApiService {

    private final SceneCaseApiRepository sceneCaseApiRepository;
    private final SceneCaseApiMapper sceneCaseApiMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SceneCaseApiLogMapper sceneCaseApiLogMapper;

    public SceneCaseApiServiceImpl(SceneCaseApiRepository sceneCaseApiRepository,
        SceneCaseApiMapper sceneCaseApiMapper,
        ApplicationEventPublisher applicationEventPublisher,
        SceneCaseApiLogMapper sceneCaseApiLogMapper) {
        this.sceneCaseApiRepository = sceneCaseApiRepository;
        this.sceneCaseApiMapper = sceneCaseApiMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.sceneCaseApiLogMapper = sceneCaseApiLogMapper;
    }

    @Override
    public void batch(AddSceneCaseApiDto addSceneCaseApiDto) {
        log.info("SceneCaseApiService-batch()-params: [SceneCaseApi]={}", addSceneCaseApiDto.toString());
        try {
            List<SceneCaseApi> caseApiList =
                sceneCaseApiMapper.toSceneCaseApiList(addSceneCaseApiDto.getSceneCaseApiList());
            sceneCaseApiRepository.insert(caseApiList);
            publishBatchSceneCaseApiEvent(caseApiList, OperationType.ADD);
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        log.info("SceneCaseApiService-deleteById()-params: [id]={}", id);
        try {
            Optional<SceneCaseApi> sceneCaseApi = sceneCaseApiRepository.findById(id);
            sceneCaseApi.ifPresent(api -> {
                sceneCaseApiRepository.deleteById(id);
                publishSceneCaseApiEvent(api, OperationType.DELETE);
            });
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseApi!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void edit(SceneCaseApiDto sceneCaseApiDto) {
        log.info("SceneCaseApiService-edit()-params: [SceneCaseApi]={}", sceneCaseApiDto.toString());
        try {
            SceneCaseApi sceneCaseApi = sceneCaseApiMapper.toSceneCaseApi(sceneCaseApiDto);
            sceneCaseApiRepository.save(sceneCaseApi);
            publishSceneCaseApiEvent(sceneCaseApi, OperationType.EDIT);
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void batchEdit(UpdateSceneCaseApiDto updateSceneCaseApiSortOrderDto) {
        log.info("SceneCaseApiService-batchEdit()-params: [SceneCaseApi]={}",
            updateSceneCaseApiSortOrderDto.toString());
        try {
            if (!updateSceneCaseApiSortOrderDto.getSceneCaseApiDtoList().isEmpty()) {
                List<SceneCaseApi> caseApiList = sceneCaseApiMapper
                    .toSceneCaseApiList(updateSceneCaseApiSortOrderDto.getSceneCaseApiDtoList());
                sceneCaseApiRepository.saveAll(caseApiList);
                publishBatchSceneCaseApiEvent(caseApiList, OperationType.EDIT);
            }
        } catch (Exception e) {
            log.error("Failed to batch edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(BATCH_EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiDto> listBySceneCaseId(String sceneCaseId, boolean remove) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).remove(remove).build());
            Sort sort = Sort.by(Direction.fromString(Direction.ASC.name()), SearchFiled.ORDER_NUMBER.getFiledName());
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
    public SceneCaseApiDto getSceneCaseApiById(String id) {
        try {
            Optional<SceneCaseApi> sceneCaseApi = sceneCaseApiRepository.findById(id);
            return sceneCaseApi.map(sceneCaseApiMapper::toSceneCaseApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi by id!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR);
        }
    }

    private void publishBatchSceneCaseApiEvent(List<SceneCaseApi> caseApiList, OperationType operationType) {
        for (SceneCaseApi sceneCaseApi : caseApiList) {
            publishSceneCaseApiEvent(sceneCaseApi, operationType);
        }
    }

    private void publishSceneCaseApiEvent(SceneCaseApi sceneCaseApi, OperationType operationType) {
        SceneCaseApiLogDto sceneCaseApiLogDto = sceneCaseApiLogMapper.toDtoBySceneCaseApi(sceneCaseApi, operationType);
        SceneCaseApiLogEvent event = new SceneCaseApiLogEvent(this, sceneCaseApiLogDto);
        applicationEventPublisher.publishEvent(event);
    }

}
