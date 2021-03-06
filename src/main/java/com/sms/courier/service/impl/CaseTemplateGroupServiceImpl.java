package com.sms.courier.service.impl;

import static com.sms.courier.common.enums.OperationModule.CASE_TEMPLATE_GROUP;
import static com.sms.courier.common.enums.OperationType.ADD;
import static com.sms.courier.common.enums.OperationType.EDIT;
import static com.sms.courier.common.enums.OperationType.REMOVE;
import static com.sms.courier.common.exception.ErrorCode.ADD_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.courier.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.courier.common.exception.ErrorCode.GET_CASE_TEMPLATE_GROUP_LIST_ERROR;
import static com.sms.courier.utils.Assert.isTrue;

import com.google.common.collect.Lists;
import com.sms.courier.common.aspect.annotation.Enhance;
import com.sms.courier.common.aspect.annotation.LogRecord;
import com.sms.courier.common.constant.Constants;
import com.sms.courier.common.exception.ApiTestPlatformException;
import com.sms.courier.common.function.FunctionHandler;
import com.sms.courier.dto.request.CaseTemplateGroupRequest;
import com.sms.courier.dto.response.TreeResponse;
import com.sms.courier.entity.group.CaseTemplateGroupEntity;
import com.sms.courier.entity.scenetest.CaseTemplateEntity;
import com.sms.courier.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.courier.mapper.CaseTemplateGroupMapper;
import com.sms.courier.repository.CaseTemplateGroupRepository;
import com.sms.courier.repository.CustomizedCaseTemplateRepository;
import com.sms.courier.service.CaseTemplateGroupService;
import com.sms.courier.service.CaseTemplateService;
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
public class CaseTemplateGroupServiceImpl implements CaseTemplateGroupService {

    private final CaseTemplateGroupRepository caseTemplateGroupRepository;
    private final CaseTemplateGroupMapper caseTemplateGroupMapper;
    private final CaseTemplateService caseTemplateService;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final DefaultIdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getSharedInstance();

    public CaseTemplateGroupServiceImpl(CaseTemplateGroupRepository caseTemplateGroupRepository,
        CaseTemplateGroupMapper caseTemplateGroupMapper,
        CaseTemplateService caseTemplateService,
        CustomizedCaseTemplateRepository customizedCaseTemplateRepository) {
        this.caseTemplateGroupRepository = caseTemplateGroupRepository;
        this.caseTemplateGroupMapper = caseTemplateGroupMapper;
        this.caseTemplateService = caseTemplateService;
        this.customizedCaseTemplateRepository = customizedCaseTemplateRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE_GROUP, template = "{{#request.name}}")
    public Boolean add(CaseTemplateGroupRequest request) {
        try {
            log.info("CaseTemplateGroupService-add()-params: [CaseTemplateGroup]={}", request.toString());
            CaseTemplateGroupEntity caseTemplateGroupEntity = caseTemplateGroupMapper
                .toCaseTemplateGroupEntity(request);
            String parentId = caseTemplateGroupEntity.getParentId();
            CaseTemplateGroupEntity parentGroup = CaseTemplateGroupEntity.builder().depth(0).build();
            if (StringUtils.isNotBlank(parentId)) {
                parentGroup = caseTemplateGroupRepository.findById(parentId)
                    .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "CaseTemplateGroup", parentId));
            }
            isTrue(parentGroup.getDepth() < Constants.MAX_DEPTH, "The group depth must be less than three.");
            Long realGroupId = identifierGenerator.nextId();
            parentGroup.getPath().add(realGroupId);
            caseTemplateGroupEntity.setDepth(parentGroup.getDepth() + 1);
            caseTemplateGroupEntity.setRealGroupId(realGroupId);
            caseTemplateGroupEntity.setPath(parentGroup.getPath());
            caseTemplateGroupRepository.insert(caseTemplateGroupEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateGroup!", e);
            throw ExceptionUtils.mpe(ADD_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_GROUP, template = "{{#request.name}}")
    public Boolean edit(CaseTemplateGroupRequest request) {
        try {
            log.info("CaseTemplateGroupService-edit()-params: [CaseTemplateGroup]={}", request.toString());
            CaseTemplateGroupEntity caseTemplateGroupEntity = caseTemplateGroupMapper
                .toCaseTemplateGroupEntity(request);
            String id = caseTemplateGroupEntity.getId();
            CaseTemplateGroupEntity oldCaseTemplateGroup = caseTemplateGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "CaseTemplateGroup", id));
            caseTemplateGroupEntity.setRealGroupId(oldCaseTemplateGroup.getRealGroupId());
            caseTemplateGroupEntity.setPath(oldCaseTemplateGroup.getPath());
            caseTemplateGroupEntity.setDepth(oldCaseTemplateGroup.getDepth());
            caseTemplateGroupEntity.setParentId(oldCaseTemplateGroup.getParentId());
            caseTemplateGroupRepository.save(caseTemplateGroupEntity);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateGroup!", e);
            throw ExceptionUtils.mpe(EDIT_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = REMOVE, operationModule = CASE_TEMPLATE_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean deleteById(String id) {
        try {
            CaseTemplateGroupEntity caseTemplateGroup = caseTemplateGroupRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.mpe(EDIT_NOT_EXIST_ERROR, "CaseTemplateGroup", id));
            //Query all CaseTemplateGroup contain children groups
            List<String> ids = Lists.newArrayList();
            if (Objects.nonNull(caseTemplateGroup.getRealGroupId())) {
                ids = caseTemplateGroupRepository
                    .findAllByPathContains(caseTemplateGroup.getRealGroupId()).map(CaseTemplateGroupEntity::getId)
                    .collect(Collectors.toList());
            }
            FunctionHandler.confirmed(CollectionUtils.isNotEmpty(ids), ids).handler(this::deleteByIds);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    public List<TreeResponse> list(String projectId) {
        try {
            List<CaseTemplateGroupEntity> caseTemplateGroupEntityList =
                caseTemplateGroupRepository.findCaseTemplateGroupEntitiesByProjectId(projectId);
            return TreeUtils.createTree(caseTemplateGroupMapper.toResponse(caseTemplateGroupEntityList));
        } catch (Exception e) {
            log.error("Failed to getList the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_GROUP_LIST_ERROR);
        }
    }

    private void deleteByIds(List<String> ids) {
        caseTemplateGroupRepository.deleteAllByIdIn(ids);
        List<CaseTemplateEntity> caseTemplateEntityList = customizedCaseTemplateRepository
            .getCaseTemplateIdsByGroupIds(ids);
        if (CollectionUtils.isNotEmpty(caseTemplateEntityList)) {
            List<String> caseTemplateIds = caseTemplateEntityList.stream().map(CaseTemplateEntity::getId)
                .collect(Collectors.toList());
            caseTemplateService.delete(caseTemplateIds);
            customizedCaseTemplateRepository.deleteGroupIdByIds(caseTemplateIds);
        }
    }

}
