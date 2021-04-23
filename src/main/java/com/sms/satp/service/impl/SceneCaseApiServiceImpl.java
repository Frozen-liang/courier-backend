package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.BATCH_EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.SearchFiled;
import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiSortOrderDto;
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
    public void batch(AddSceneCaseApiDto addSceneCaseApiDto) {
        try {
            List<SceneCaseApi> caseApiList =
                sceneCaseApiMapper.toSceneCaseApiList(addSceneCaseApiDto.getSceneCaseApiList());
            sceneCaseApiRepository.insert(caseApiList);
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseApi!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            sceneCaseApiRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseApi!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void edit(SceneCaseApiDto sceneCaseApiDto) {
        try {
            SceneCaseApi sceneCaseApi = sceneCaseApiMapper.toSceneCaseApi(sceneCaseApiDto);
            sceneCaseApiRepository.save(sceneCaseApi);
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void batchEdit(UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto) {
        try {
            if (!updateSceneCaseApiSortOrderDto.getSceneCaseApiDtoList().isEmpty()) {
                List<SceneCaseApi> caseApiList = sceneCaseApiMapper
                    .toSceneCaseApiList(updateSceneCaseApiSortOrderDto.getSceneCaseApiDtoList());
                sceneCaseApiRepository.saveAll(caseApiList);
            }
            //edit template case api list
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
            //query template case api

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
}
