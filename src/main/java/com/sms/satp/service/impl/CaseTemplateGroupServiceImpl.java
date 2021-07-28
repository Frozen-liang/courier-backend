package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.CASE_TEMPLATE_GROUP;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.enums.OperationType.REMOVE;
import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_NOT_EXIST_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_GROUP_LIST_ERROR;
import static com.sms.satp.utils.Assert.isTrue;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.constant.Constants;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.dto.request.CaseTemplateGroupRequest;
import com.sms.satp.dto.response.TreeResponse;
import com.sms.satp.entity.group.CaseTemplateGroupEntity;
import com.sms.satp.entity.scenetest.CaseTemplateEntity;
import com.sms.satp.infrastructure.id.DefaultIdentifierGenerator;
import com.sms.satp.mapper.CaseTemplateGroupMapper;
import com.sms.satp.repository.CaseTemplateGroupRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.CaseTemplateGroupService;
import com.sms.satp.service.CaseTemplateService;
import com.sms.satp.utils.ExceptionUtils;
import com.sms.satp.utils.TreeUtils;
import java.util.List;
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
            List<String> ids = caseTemplateGroupRepository
                .findAllByPathContains(caseTemplateGroup.getRealGroupId()).map(CaseTemplateGroupEntity::getId)
                .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ids)) {
                caseTemplateGroupRepository.deleteAllByIdIn(ids);
                List<CaseTemplateEntity> caseTemplateEntityList = customizedCaseTemplateRepository
                    .getCaseTemplateIdsByGroupIds(ids);
                if (CollectionUtils.isNotEmpty(caseTemplateEntityList)) {
                    List<String> caseTemplateIds = caseTemplateEntityList.stream().map(CaseTemplateEntity::getId)
                        .collect(Collectors.toList());
                    caseTemplateService.delete(caseTemplateIds);
                }
            }
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

}
