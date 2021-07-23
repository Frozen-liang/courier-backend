package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.SCENE_CASE_GROUP;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_SCENE_CASE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_GROUP_LIST_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.AddSceneCaseGroupRequest;
import com.sms.satp.dto.request.SearchSceneCaseGroupRequest;
import com.sms.satp.dto.request.UpdateSceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import com.sms.satp.entity.group.SceneCaseGroupEntity;
import com.sms.satp.entity.scenetest.SceneCaseEntity;
import com.sms.satp.mapper.SceneCaseGroupMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseGroupRepository;
import com.sms.satp.service.SceneCaseGroupService;
import com.sms.satp.service.SceneCaseService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseGroupServiceImpl implements SceneCaseGroupService {

    private final SceneCaseGroupRepository sceneCaseGroupRepository;
    private final SceneCaseGroupMapper sceneCaseGroupMapper;
    private final SceneCaseService sceneCaseService;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;

    public SceneCaseGroupServiceImpl(SceneCaseGroupRepository sceneCaseGroupRepository,
        SceneCaseGroupMapper sceneCaseGroupMapper, SceneCaseService sceneCaseService,
        CustomizedSceneCaseRepository customizedSceneCaseRepository) {
        this.sceneCaseGroupRepository = sceneCaseGroupRepository;
        this.sceneCaseGroupMapper = sceneCaseGroupMapper;
        this.sceneCaseService = sceneCaseService;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_GROUP, template = "{{#request.name}}")
    public Boolean add(AddSceneCaseGroupRequest request) {
        try {
            log.info("SceneCaseGroupService-add()-params: [SceneCaseGroup]={}", request.toString());
            SceneCaseGroupEntity caseGroup = sceneCaseGroupMapper.toSceneCaseGroupByAdd(request);
            sceneCaseGroupRepository.insert(caseGroup);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseGroup!", e);
            throw new ApiTestPlatformException(ADD_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_GROUP, template = "{{#request.name}}")
    public Boolean edit(UpdateSceneCaseGroupRequest request) {
        try {
            log.info("SceneCaseGroupService-edit()-params: [SceneCaseGroup]={}", request.toString());
            SceneCaseGroupEntity caseGroup = sceneCaseGroupMapper.toSceneCaseGroupByUpdate(request);
            Optional<SceneCaseGroupEntity> optional = sceneCaseGroupRepository.findById(caseGroup.getId());
            optional.ifPresent(sceneCaseGroup -> sceneCaseGroupRepository.save(caseGroup));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseGroup!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = SCENE_CASE_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean deleteById(String id) {
        try {
            Optional<SceneCaseGroupEntity> sceneCaseGroup = sceneCaseGroupRepository.findById(id);
            sceneCaseGroup.ifPresent(caseGroup -> {
                sceneCaseGroupRepository.deleteById(caseGroup.getId());
                List<SceneCaseEntity> sceneCaseEntityList = customizedSceneCaseRepository.getIdsByGroupId(id);
                if (CollectionUtils.isNotEmpty(sceneCaseEntityList)) {
                    List<String> sceneCaseIds = sceneCaseEntityList.stream().map(SceneCaseEntity::getId)
                        .collect(Collectors.toList());
                    sceneCaseService.delete(sceneCaseIds);
                }
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseGroup!", e);
            throw new ApiTestPlatformException(DELETE_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    public List<SceneCaseGroupResponse> getList(SearchSceneCaseGroupRequest request) {
        try {
            SceneCaseGroupEntity group =
                SceneCaseGroupEntity.builder().projectId(request.getProjectId()).parentId(request.getParentId())
                    .build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.NAME.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.PARENT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<SceneCaseGroupEntity> example = Example.of(group, exampleMatcher);
            List<SceneCaseGroupEntity> sceneCaseGroups = sceneCaseGroupRepository.findAll(example);
            return sceneCaseGroupMapper.toResponseList(sceneCaseGroups);
        } catch (Exception e) {
            log.error("Failed to getList the SceneCase page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_GROUP_LIST_ERROR);
        }
    }

}
