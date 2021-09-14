package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.SCENE_CASE_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.DELETE;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.exception.ErrorCode.ADD_SCENE_CASE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_SCENE_CASE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_SCENE_CASE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_SCENE_CASE_GROUP_LIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.dto.request.SceneCaseGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.group.SceneCaseGroupEntity;
import com.sms.courier.entity.scenetest.SceneCaseEntity;
import com.sms.courier.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.courier.mapper.SceneCaseGroupMapper;
import com.sms.courier.repository.CustomizedSceneCaseRepository;
import com.sms.courier.repository.SceneCaseGroupRepository;
import com.sms.courier.service.SceneCaseGroupService;
import com.sms.courier.service.SceneCaseService;
import com.sms.courier.utils.ExceptionUtils;
import com.sms.courier.utils.TreeUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseGroupServiceImpl implements SceneCaseGroupService {

    private final SceneCaseGroupRepository sceneCaseGroupRepository;
    private final SceneCaseGroupMapper sceneCaseGroupMapper;
    private final SceneCaseService sceneCaseService;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final DefaultIdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getSharedInstance();

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
    public Boolean add(SceneCaseGroupRequest request) {
        try {
            log.info("SceneCaseGroupService-add()-params: [SceneCaseGroup]={}", request.toString());
            SceneCaseGroupEntity sceneCaseGroupEntity = sceneCaseGroupMapper.toSceneCaseGroupEntity(request);
            String parentId = sceneCaseGroupEntity.getParentId();
            SceneCaseGroupEntity parentGroup = SceneCaseGroupEntity.builder().depth(0).build();
            if (StringUtils.isNotBlank(parentId)) {
                parentGroup = sceneCaseGroupRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "SceneCaseGroup", parentId));
            }
            isTrue(parentGroup.getDepth() < Constants.MAX_DEPTH, "The group depth must be less than three.");
            Long realGroupId = identifierGenerator.nextId();
            parentGroup.getPath().add(realGroupId);
            sceneCaseGroupEntity.setDepth(parentGroup.getDepth() + 1);
            sceneCaseGroupEntity.setRealGroupId(realGroupId);
            sceneCaseGroupEntity.setPath(parentGroup.getPath());
            sceneCaseGroupRepository.insert(sceneCaseGroupEntity);
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to add the SceneCaseGroup!", e);
            throw ExceptionUtils.mpe(ADD_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = SCENE_CASE_GROUP, template = "{{#request.name}}")
    public Boolean edit(SceneCaseGroupRequest request) {
        try {
            log.info("SceneCaseGroupService-edit()-params: [SceneCaseGroup]={}", request.toString());
            SceneCaseGroupEntity sceneCaseGroupEntity = sceneCaseGroupMapper.toSceneCaseGroupEntity(request);
            String id = sceneCaseGroupEntity.getId();
            SceneCaseGroupEntity oldSceneCaseGroup = sceneCaseGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "SceneCaseGroup", id));
            sceneCaseGroupEntity.setRealGroupId(oldSceneCaseGroup.getRealGroupId());
            sceneCaseGroupEntity.setPath(oldSceneCaseGroup.getPath());
            sceneCaseGroupEntity.setDepth(oldSceneCaseGroup.getDepth());
            sceneCaseGroupEntity.setParentId(oldSceneCaseGroup.getParentId());
            sceneCaseGroupRepository.save(sceneCaseGroupEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseGroup!", e);
            throw ExceptionUtils.mpe(EDIT_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean deleteById(String id) {
        try {
            SceneCaseGroupEntity sceneCaseGroupEntity = sceneCaseGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "SceneCaseGroup", id));
            //Query all sceneCaseGroup contain children groups
            List<String> ids = Lists.newArrayList();
            if (Objects.nonNull(sceneCaseGroupEntity.getRealGroupId())) {
                ids = sceneCaseGroupRepository
                    .findAllByPathContains(sceneCaseGroupEntity.getRealGroupId()).map(SceneCaseGroupEntity::getId)
                    .collect(Collectors.toList());
            }
            if (CollectionUtils.isNotEmpty(ids)) {
                sceneCaseGroupRepository.deleteAllByIdIn(ids);
                List<SceneCaseEntity> sceneCaseEntityList = customizedSceneCaseRepository
                    .getSceneCaseIdsByGroupIds(ids);
                if (CollectionUtils.isNotEmpty(sceneCaseEntityList)) {
                    List<String> sceneCaseIds = sceneCaseEntityList.stream().map(SceneCaseEntity::getId)
                        .collect(Collectors.toList());
                    sceneCaseService.delete(sceneCaseIds);
                    customizedSceneCaseRepository.deleteGroupIdByIds(sceneCaseIds);
                }
            }
            return Boolean.TRUE;
        } catch (ApiTestPlatformException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete the SceneCaseGroup!", e);
            throw ExceptionUtils.mpe(DELETE_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    public List<TreeResponse> list(String projectId) {
        try {
            List<SceneCaseGroupEntity> sceneCaseGroups =
                sceneCaseGroupRepository.findSceneCaseGroupEntitiesByProjectId(projectId);
            return TreeUtils.createTree(sceneCaseGroupMapper.toResponse(sceneCaseGroups));
        } catch (Exception e) {
            log.error("Failed to getList the SceneCase page!", e);
            throw ExceptionUtils.mpe(GET_SCENE_CASE_GROUP_LIST_ERROR);
        }
    }

}
