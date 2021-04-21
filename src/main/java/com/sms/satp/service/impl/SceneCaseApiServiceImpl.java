package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_API_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_API_SORT_ORDER_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_BY_ID_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_API_LIST_BY_SCENE_CASE_ID_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.SearchFiled;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.entity.dto.AddSceneCaseApiDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.SortOrderDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiSortOrderDto;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.SceneCaseApiMapper;
import com.sms.satp.repository.SceneCaseApiRepository;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
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
            for (SceneCaseApiDto dto : addSceneCaseApiDto.getSceneCaseApiList()) {
                SceneCaseApi caseApi = sceneCaseApiMapper.toSceneCaseApi(dto);
                caseApi.setId(new ObjectId().toString());
                caseApi.setSceneCaseId(addSceneCaseApiDto.getSceneCaseId());
                caseApi.setStatus(Constants.STATUS_VALID);
                caseApi.setCreateUserId(addSceneCaseApiDto.getCurrentUserId());
                sceneCaseApiRepository.insert(caseApi);
            }
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
    public void edit(UpdateSceneCaseApiDto updateSceneCaseApiDto) {
        try {
            SceneCaseApiDto dto = updateSceneCaseApiDto.getSceneCaseApiDto();
            SceneCaseApi sceneCaseApi = sceneCaseApiMapper.toSceneCaseApi(dto);
            sceneCaseApi.setModifyUserId(updateSceneCaseApiDto.getCurrentUserId());
            sceneCaseApiRepository.save(sceneCaseApi);
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_ERROR);
        }
    }

    @Override
    public void editSortOrder(UpdateSceneCaseApiSortOrderDto updateSceneCaseApiSortOrderDto) {
        try {
            for (SortOrderDto sortOrderDto : updateSceneCaseApiSortOrderDto.getSortOrderDtoList()) {
                if (Objects.nonNull(sortOrderDto.getSceneCaseApiId())) {
                    updateSceneCaseApiSortOrder(updateSceneCaseApiSortOrderDto.getSceneCaseId(),
                        updateSceneCaseApiSortOrderDto.getCurrentUserId(), sortOrderDto);
                }
                if (Objects.nonNull(sortOrderDto.getTemplateApiId())) {
                    updateTemplateApiSortOrder(updateSceneCaseApiSortOrderDto.getSceneCaseId(),
                        updateSceneCaseApiSortOrderDto.getCurrentUserId(), sortOrderDto);
                }
            }
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseApi sort order!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_API_SORT_ORDER_ERROR);
        }
    }

    @Override
    public List<SceneCaseApiDto> list(String sceneCaseId) {
        try {
            Example<SceneCaseApi> example = Example.of(
                SceneCaseApi.builder().sceneCaseId(sceneCaseId).status(Constants.STATUS_VALID).build());
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
    public SceneCaseApiDto getSceneCaseApiById(String id) {
        try {
            Optional<SceneCaseApi> sceneCaseApi = sceneCaseApiRepository.findById(id);
            return sceneCaseApi.map(sceneCaseApiMapper::toSceneCaseApiDto).orElse(null);
        } catch (Exception e) {
            log.error("Failed to get the SceneCaseApi by id!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_API_BY_ID_ERROR);
        }
    }

    private void updateSceneCaseApiSortOrder(String sceneCaseId, String currentUserId, SortOrderDto sortOrderDto) {
        Example<SceneCaseApi> example = Example.of(
            SceneCaseApi.builder().sceneCaseId(sceneCaseId).apiId(sortOrderDto.getSceneCaseApiId()).build());
        Optional<SceneCaseApi> sceneCaseApi = sceneCaseApiRepository.findOne(example);
        sceneCaseApi.ifPresent(caseApi -> {
            caseApi.setOrderNumber(sortOrderDto.getOrderNumber());
            caseApi.setModifyUserId(currentUserId);
            sceneCaseApiRepository.save(caseApi);
        });
    }

    private void updateTemplateApiSortOrder(String sceneCaseId, String currentUserId, SortOrderDto sortOrderDto) {
        //edit template api sort order.
    }

}
