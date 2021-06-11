package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.SCENE_CASE_GROUP;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
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
import com.sms.satp.dto.request.SearchSceneCaseRequest;
import com.sms.satp.dto.request.UpdateSceneCaseGroupRequest;
import com.sms.satp.dto.response.SceneCaseGroupResponse;
import com.sms.satp.entity.group.SceneCaseGroup;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.mapper.SceneCaseGroupMapper;
import com.sms.satp.repository.CustomizedSceneCaseRepository;
import com.sms.satp.repository.SceneCaseGroupRepository;
import com.sms.satp.service.SceneCaseGroupService;
import com.sms.satp.service.SceneCaseService;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SceneCaseGroupServiceImpl implements SceneCaseGroupService {

    private final SceneCaseGroupRepository sceneCaseGroupRepository;
    private final SceneCaseGroupMapper sceneCaseGroupMapper;
    private final CustomizedSceneCaseRepository customizedSceneCaseRepository;
    private final SceneCaseService sceneCaseService;

    public SceneCaseGroupServiceImpl(SceneCaseGroupRepository sceneCaseGroupRepository,
        SceneCaseGroupMapper sceneCaseGroupMapper,
        CustomizedSceneCaseRepository customizedSceneCaseRepository,
        SceneCaseService sceneCaseService) {
        this.sceneCaseGroupRepository = sceneCaseGroupRepository;
        this.sceneCaseGroupMapper = sceneCaseGroupMapper;
        this.customizedSceneCaseRepository = customizedSceneCaseRepository;
        this.sceneCaseService = sceneCaseService;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = SCENE_CASE_GROUP, template = "{{#request.name}}")
    public Boolean add(AddSceneCaseGroupRequest request) {
        try {
            log.info("SceneCaseGroupService-add()-params: [SceneCaseGroup]={}", request.toString());
            SceneCaseGroup caseGroup = sceneCaseGroupMapper.toSceneCaseGroupByAdd(request);
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
            SceneCaseGroup caseGroup = sceneCaseGroupMapper.toSceneCaseGroupByUpdate(request);
            Optional<SceneCaseGroup> optional = sceneCaseGroupRepository.findById(caseGroup.getId());
            optional.ifPresent(sceneCaseGroup -> sceneCaseGroupRepository.save(caseGroup));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the SceneCaseGroup!", e);
            throw new ApiTestPlatformException(EDIT_SCENE_CASE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = SCENE_CASE_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean deleteById(String id) {
        try {
            Optional<SceneCaseGroup> sceneCaseGroup = sceneCaseGroupRepository.findById(id);
            sceneCaseGroup.ifPresent(caseGroup -> {
                sceneCaseGroupRepository.deleteById(caseGroup.getId());
                editSceneCaseStatus(caseGroup.getId(), caseGroup.getProjectId());
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
            SceneCaseGroup group =
                SceneCaseGroup.builder().projectId(request.getProjectId()).parentId(request.getParentId()).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.NAME.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.PARENT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<SceneCaseGroup> example = Example.of(group, exampleMatcher);
            List<SceneCaseGroup> sceneCaseGroups = sceneCaseGroupRepository.findAll(example);
            return sceneCaseGroupMapper.toResponseList(sceneCaseGroups);
        } catch (Exception e) {
            log.error("Failed to getList the SceneCase page!", e);
            throw new ApiTestPlatformException(GET_SCENE_CASE_GROUP_LIST_ERROR);
        }
    }

    private void editSceneCaseStatus(String id, String projectId) {
        SearchSceneCaseRequest request = SearchSceneCaseRequest.builder().groupId(id).build();
        Page<SceneCase> sceneCasePage = customizedSceneCaseRepository.search(request, projectId);
        List<SceneCase> sceneCaseList = sceneCasePage.getContent();
        if (CollectionUtils.isNotEmpty(sceneCaseList)) {
            for (SceneCase sceneCase : sceneCaseList) {
                sceneCase.setRemoved(true);
            }
            sceneCaseService.batchEdit(sceneCaseList);
        }
    }

}
