package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.CASE_TEMPLATE_GROUP;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_GROUP_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_GROUP_LIST_ERROR;

import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.common.field.SceneFiled;
import com.sms.satp.dto.request.AddCaseTemplateGroupRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.SearchCaseTemplateGroupRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateGroupRequest;
import com.sms.satp.dto.response.CaseTemplateGroupResponse;
import com.sms.satp.entity.group.CaseTemplateGroup;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.mapper.CaseTemplateGroupMapper;
import com.sms.satp.repository.CaseTemplateGroupRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.service.CaseTemplateGroupService;
import com.sms.satp.service.CaseTemplateService;
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
public class CaseTemplateGroupServiceImpl implements CaseTemplateGroupService {

    private final CaseTemplateGroupRepository caseTemplateGroupRepository;
    private final CaseTemplateGroupMapper caseTemplateGroupMapper;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final CaseTemplateService caseTemplateService;

    public CaseTemplateGroupServiceImpl(CaseTemplateGroupRepository caseTemplateGroupRepository,
        CaseTemplateGroupMapper caseTemplateGroupMapper,
        CustomizedCaseTemplateRepository customizedCaseTemplateRepository,
        CaseTemplateService caseTemplateService) {
        this.caseTemplateGroupRepository = caseTemplateGroupRepository;
        this.caseTemplateGroupMapper = caseTemplateGroupMapper;
        this.customizedCaseTemplateRepository = customizedCaseTemplateRepository;
        this.caseTemplateService = caseTemplateService;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE_GROUP, template = "{{#request.name}}")
    public Boolean add(AddCaseTemplateGroupRequest request) {
        try {
            log.info("CaseTemplateGroupService-add()-params: [CaseTemplateGroup]={}", request.toString());
            CaseTemplateGroup caseGroup = caseTemplateGroupMapper.toCaseTemplateGroupByAdd(request);
            caseTemplateGroupRepository.insert(caseGroup);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE_GROUP, template = "{{#request.name}}")
    public Boolean edit(UpdateCaseTemplateGroupRequest request) {
        try {
            log.info("CaseTemplateGroupService-edit()-params: [CaseTemplateGroup]={}", request.toString());
            CaseTemplateGroup caseGroup = caseTemplateGroupMapper.toCaseTemplateGroupByUpdate(request);
            Optional<CaseTemplateGroup> optional = caseTemplateGroupRepository.findById(caseGroup.getId());
            optional.ifPresent(caseTemplateGroup -> caseTemplateGroupRepository.save(caseGroup));
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = CASE_TEMPLATE_GROUP, template = "{{#result?.name}}",
        enhance = @Enhance(enable = true))
    public Boolean deleteById(String id) {
        try {
            Optional<CaseTemplateGroup> caseTemplateGroup = caseTemplateGroupRepository.findById(id);
            caseTemplateGroup.ifPresent(caseGroup -> {
                caseTemplateGroupRepository.deleteById(caseGroup.getId());
                editCaseTemplateStatus(caseGroup.getId(), caseGroup.getProjectId());
            });
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_GROUP_ERROR);
        }
    }

    @Override
    public List<CaseTemplateGroupResponse> getList(SearchCaseTemplateGroupRequest request) {
        try {
            CaseTemplateGroup group =
                CaseTemplateGroup.builder().projectId(request.getProjectId()).parentId(request.getParentId()).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.NAME.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(SceneFiled.PARENT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<CaseTemplateGroup> example = Example.of(group, exampleMatcher);
            List<CaseTemplateGroup> caseTemplateGroups = caseTemplateGroupRepository.findAll(example);
            return caseTemplateGroupMapper.toResponseList(caseTemplateGroups);
        } catch (Exception e) {
            log.error("Failed to getList the CaseTemplateGroup!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_GROUP_LIST_ERROR);
        }
    }

    private void editCaseTemplateStatus(String id, String projectId) {
        CaseTemplateSearchRequest request = CaseTemplateSearchRequest.builder().groupId(id).build();
        Page<CaseTemplate> caseTemplatePage = customizedCaseTemplateRepository.search(request, projectId);
        List<CaseTemplate> caseTemplateList = caseTemplatePage.getContent();
        if (CollectionUtils.isNotEmpty(caseTemplateList)) {
            for (CaseTemplate caseTemplate : caseTemplateList) {
                caseTemplate.setRemoved(true);
            }
            caseTemplateService.batchEdit(caseTemplateList);
        }
    }

}