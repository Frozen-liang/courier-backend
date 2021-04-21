package com.sms.satp.service.impl;

import static com.sms.satp.common.ErrorCode.ADD_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.DELETE_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.EDIT_SCENE_CASE_ERROR;
import static com.sms.satp.common.ErrorCode.GET_SCENE_CASE_PAGE_ERROR;
import static com.sms.satp.common.ErrorCode.SEARCH_SCENE_CASE_ERROR;

import com.sms.satp.common.ApiTestPlatformException;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.entity.dto.PageDto;
import com.sms.satp.entity.dto.AddSceneCaseDto;
import com.sms.satp.entity.dto.SceneCaseApiDto;
import com.sms.satp.entity.dto.UpdateSceneCaseApiDto;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.dto.SceneCaseDto;
import com.sms.satp.entity.dto.SceneCaseSearchDto;
import com.sms.satp.entity.dto.UpdateSceneCaseDto;
import com.sms.satp.mapper.SceneCaseMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.SceneCaseApiService;
import com.sms.satp.service.SceneCaseService;
import com.sms.satp.utils.PageDtoConverter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseServiceImpl implements SceneCaseService {

    private final SceneCaseRepository sceneCaseRepository;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final SceneCaseMapper sceneCaseMapper;
    private final SceneCaseApiService sceneCaseApiService;

    public SceneCaseServiceImpl(SceneCaseRepository sceneCaseRepository,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseMapper sceneCaseMapper, SceneCaseApiService sceneCaseApiService) {
        this.sceneCaseRepository = sceneCaseRepository;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseMapper = sceneCaseMapper;
        this.sceneCaseApiService = sceneCaseApiService;
    }

    @Override
    public void add(AddSceneCaseDto sceneCaseDto) {
        try {
            SceneCase sceneCase = sceneCaseMapper.toAddSceneCase(sceneCaseDto);
            sceneCase.setId(new ObjectId().toString());
            sceneCase.setStatus(Constants.STATUS_VALID);
            //query user by "createUserId",write for filed createUserName.
            sceneCase.setCreateDateTime(LocalDateTime.now());
            sceneCaseRepository.insert(sceneCase);
        } catch (Exception e) {
            log.error("Failed to add the SceneCase!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_ERROR);
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            sceneCaseRepository.deleteById(id);
            List<SceneCaseApiDto> sceneCaseApiDtoList = sceneCaseApiService.listBySceneCaseId(id);
            if (!sceneCaseApiDtoList.isEmpty()) {
                deleteSceneCaseApi(sceneCaseApiDtoList);
            }
        } catch (Exception e) {
            log.error("Failed to delete the SceneCase!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_ERROR);
        }
    }

    private void deleteSceneCaseApi(List<SceneCaseApiDto> sceneCaseApiDtoList) {
        for (SceneCaseApiDto dto : sceneCaseApiDtoList) {
            sceneCaseApiService.deleteById(dto.getId());
        }
    }

    @Override
    public void edit(UpdateSceneCaseDto sceneCaseDto) {
        try {
            SceneCase sceneCase = sceneCaseMapper.toUpdateSceneCase(sceneCaseDto);
            Optional<SceneCase> optionalSceneCase = sceneCaseRepository.findById(sceneCase.getId());
            optionalSceneCase.ifPresent(sceneCaseFindById -> {
                sceneCase.setCreateUserId(sceneCaseFindById.getCreateUserId());
                sceneCase.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
                sceneCase.setCreateUserName(sceneCaseFindById.getCreateUserName());
                sceneCase.setProjectId(sceneCaseFindById.getProjectId());
                sceneCase.setModifyDateTime(LocalDateTime.now());
                sceneCase.setStatus(
                    Objects.isNull(sceneCase.getStatus()) ? sceneCaseFindById.getStatus() : sceneCase.getStatus());
                sceneCaseRepository.save(sceneCase);
                if (!Objects.equals(sceneCase.getStatus(), sceneCaseFindById.getStatus())) {
                    editSceneCaseApiStatus(sceneCase.getId(), sceneCase.getStatus(), sceneCaseDto.getModifyUserId());
                }
            });
        } catch (Exception e) {
            log.error("Failed to edit the SceneCase!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseDto> page(PageDto pageDto, String projectId) {
        try {
            PageDtoConverter.frontMapping(pageDto);
            SceneCase sceneCase = SceneCase.builder()
                .projectId(projectId)
                .status(Constants.STATUS_VALID)
                .build();
            Example<SceneCase> example = Example.of(sceneCase);
            Sort sort = Sort.by(Direction.fromString(pageDto.getOrder()), pageDto.getSort());
            Pageable pageable = PageRequest.of(
                pageDto.getPageNumber(), pageDto.getPageSize(), sort);
            return sceneCaseRepository.findAll(example, pageable)
                .map(sceneCaseMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to get the SceneCase page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_PAGE_ERROR);
        }
    }

    @Override
    public Page<SceneCaseDto> search(SceneCaseSearchDto searchDto, String projectId) {
        try {
            Page<SceneCase> resultPage = customizedSceneCaseRepository.search(searchDto, projectId);
            return resultPage.map(sceneCaseMapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search the SceneCase!", e);
            throw new ApiTestPlatformException(SEARCH_SCENE_CASE_ERROR);
        }
    }

    private void editSceneCaseApiStatus(String sceneCaseId, Integer status, String modifyUserId) {
        List<SceneCaseApiDto> sceneCaseApiDtoList = sceneCaseApiService.listBySceneCaseId(sceneCaseId);
        if (!sceneCaseApiDtoList.isEmpty()) {
            for (SceneCaseApiDto dto : sceneCaseApiDtoList) {
                dto.setStatus(status);
                sceneCaseApiService.edit(UpdateSceneCaseApiDto.builder()
                    .sceneCaseApiDto(dto)
                    .currentUserId(modifyUserId)
                    .build());
            }
        }
    }

}
