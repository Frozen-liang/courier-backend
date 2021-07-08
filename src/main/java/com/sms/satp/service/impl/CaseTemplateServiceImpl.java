package com.sms.satp.service.impl;

import static com.sms.satp.common.enums.OperationModule.CASE_TEMPLATE;
import static com.sms.satp.common.enums.OperationType.ADD;
import static com.sms.satp.common.enums.OperationType.DELETE;
import static com.sms.satp.common.enums.OperationType.EDIT;
import static com.sms.satp.common.exception.ErrorCode.ADD_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.DELETE_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.EDIT_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_CASE_TEMPLATE_ERROR;
import static com.sms.satp.common.exception.ErrorCode.GET_SCENE_CASE_BY_ID_ERROR;
import static com.sms.satp.common.exception.ErrorCode.SEARCH_CASE_TEMPLATE_ERROR;

import com.google.common.collect.Lists;
import com.sms.satp.common.aspect.annotation.Enhance;
import com.sms.satp.common.aspect.annotation.LogRecord;
import com.sms.satp.common.exception.ApiTestPlatformException;
import com.sms.satp.common.field.CommonFiled;
import com.sms.satp.dto.request.AddCaseTemplateRequest;
import com.sms.satp.dto.request.CaseTemplateSearchRequest;
import com.sms.satp.dto.request.UpdateCaseTemplateRequest;
import com.sms.satp.dto.response.CaseTemplateApiResponse;
import com.sms.satp.dto.response.CaseTemplateDetailResponse;
import com.sms.satp.dto.response.CaseTemplateResponse;
import com.sms.satp.dto.response.IdResponse;
import com.sms.satp.entity.scenetest.CaseTemplate;
import com.sms.satp.entity.scenetest.CaseTemplateApi;
import com.sms.satp.entity.scenetest.CaseTemplateApiConn;
import com.sms.satp.entity.scenetest.CaseTemplateConn;
import com.sms.satp.entity.scenetest.SceneCase;
import com.sms.satp.entity.scenetest.SceneCaseApi;
import com.sms.satp.mapper.CaseTemplateApiMapper;
import com.sms.satp.mapper.CaseTemplateMapper;
import com.sms.satp.repository.CaseTemplateApiRepository;
import com.sms.satp.repository.CaseTemplateConnRepository;
import com.sms.satp.repository.CaseTemplateRepository;
import com.sms.satp.repository.CustomizedCaseTemplateRepository;
import com.sms.satp.repository.SceneCaseRepository;
import com.sms.satp.service.CaseTemplateApiService;
import com.sms.satp.service.CaseTemplateConnService;
import com.sms.satp.service.CaseTemplateService;
import com.sms.satp.service.SceneCaseApiService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CaseTemplateServiceImpl implements CaseTemplateService {

    private final CaseTemplateRepository caseTemplateRepository;
    private final CustomizedCaseTemplateRepository customizedCaseTemplateRepository;
    private final CaseTemplateMapper caseTemplateMapper;
    private final CaseTemplateApiService caseTemplateApiService;
    private final CaseTemplateConnService caseTemplateConnService;
    private final SceneCaseRepository sceneCaseRepository;
    private final SceneCaseApiService sceneCaseApiService;
    private final CaseTemplateConnRepository caseTemplateConnRepository;
    private final CaseTemplateApiMapper caseTemplateApiMapper;
    private final CaseTemplateApiRepository caseTemplateApiRepository;

    public CaseTemplateServiceImpl(CaseTemplateRepository sceneCaseRepository,
        CustomizedCaseTemplateRepository customizedSceneCaseRepository,
        CaseTemplateMapper sceneCaseMapper, CaseTemplateApiService sceneCaseApiService,
        CaseTemplateConnService caseTemplateConnService,
        SceneCaseRepository sceneCaseRepository1, SceneCaseApiService sceneCaseApiService1,
        CaseTemplateConnRepository caseTemplateConnRepository,
        CaseTemplateApiMapper caseTemplateApiMapper,
        CaseTemplateApiRepository caseTemplateApiRepository) {
        this.caseTemplateRepository = sceneCaseRepository;
        this.customizedCaseTemplateRepository = customizedSceneCaseRepository;
        this.caseTemplateMapper = sceneCaseMapper;
        this.caseTemplateApiService = sceneCaseApiService;
        this.caseTemplateConnService = caseTemplateConnService;
        this.sceneCaseRepository = sceneCaseRepository1;
        this.sceneCaseApiService = sceneCaseApiService1;
        this.caseTemplateConnRepository = caseTemplateConnRepository;
        this.caseTemplateApiMapper = caseTemplateApiMapper;
        this.caseTemplateApiRepository = caseTemplateApiRepository;
    }

    @Override
    @LogRecord(operationType = ADD, operationModule = CASE_TEMPLATE, template = "{{#addCaseTemplateRequest.name}}")
    public Boolean add(AddCaseTemplateRequest addCaseTemplateRequest) {
        log.info("CaseTemplateService-add()-params: [CaseTemplate]={}", addCaseTemplateRequest.toString());
        try {
            CaseTemplate sceneCaseTemplate = caseTemplateMapper.toCaseTemplateByAddRequest(addCaseTemplateRequest);
            //query user by "createUserId",write for filed createUserName.
            caseTemplateRepository.insert(sceneCaseTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public IdResponse add(String sceneCaseId) {
        try {
            Optional<SceneCase> sceneCase = sceneCaseRepository.findById(sceneCaseId);
            if (sceneCase.isEmpty()) {
                throw new ApiTestPlatformException(GET_SCENE_CASE_BY_ID_ERROR);
            }
            CaseTemplate caseTemplate = caseTemplateMapper.toCaseTemplateBySceneCase(sceneCase.get());
            caseTemplateRepository.insert(caseTemplate);
            List<SceneCaseApi> sceneCaseApiList = sceneCaseApiService.listBySceneCaseId(sceneCaseId);
            List<CaseTemplateApi> caseTemplateApiList = Lists.newArrayList();
            List<CaseTemplateApi> sceneCaseApiCaseTemplate =
                caseTemplateApiMapper.toCaseTemplateApiListBySceneCaseApiList(sceneCaseApiList);
            setCaseTemplateId(sceneCaseApiCaseTemplate, caseTemplate.getId());
            caseTemplateApiList.addAll(sceneCaseApiCaseTemplate);
            List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnRepository.findAllBySceneCaseId(sceneCaseId);
            for (CaseTemplateConn caseTemplateConn : caseTemplateConnList) {
                List<String> caseTemplateApiIds = caseTemplateConn.getCaseTemplateApiConnList().stream()
                    .map(CaseTemplateApiConn::getCaseTemplateApiId).collect(Collectors.toList());
                Map<String, Integer> caseTemplateApiOrder = caseTemplateConn.getCaseTemplateApiConnList().stream()
                    .collect(
                        Collectors.toMap(CaseTemplateApiConn::getCaseTemplateApiId, CaseTemplateApiConn::getOrder));
                List<CaseTemplateApi> oldCaseTemplateApi = caseTemplateApiRepository
                    .findAllByIdIsIn(caseTemplateApiIds);
                resetOrderAndIds(oldCaseTemplateApi, caseTemplateApiOrder, caseTemplate.getId());
                caseTemplateApiList.addAll(oldCaseTemplateApi);
            }
            if (CollectionUtils.isNotEmpty(caseTemplateApiList)) {
                caseTemplateApiRepository.insert(caseTemplateApiList);
            }
            return IdResponse.builder().id(caseTemplate.getId()).build();
        } catch (Exception e) {
            log.error("Failed to add the CaseTemplate!", e);
            throw new ApiTestPlatformException(ADD_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = DELETE, operationModule = CASE_TEMPLATE, template = "{{#result?.![#this.name]}}",
        enhance = @Enhance(enable = true, primaryKey = "ids"))
    public Boolean deleteByIds(List<String> ids) {
        log.info("CaseTemplateService-deleteById()-params: [id]={}", ids);
        try {
            for (String id : ids) {
                caseTemplateRepository.deleteById(id);
                List<CaseTemplateApi> caseTemplateApiList = caseTemplateApiService.listByCaseTemplateId(id);
                deleteCaseTemplateApi(caseTemplateApiList);
                List<CaseTemplateConn> caseTemplateConnList = caseTemplateConnService.listByCassTemplateId(id);
                deleteCaseTemplateConn(caseTemplateConnList);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to delete the CaseTemplate!", e);
            throw new ApiTestPlatformException(DELETE_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE, template = "{{#updateCaseTemplateRequest.name}}")
    public Boolean edit(UpdateCaseTemplateRequest updateCaseTemplateRequest) {
        log.info("CaseTemplateService-edit()-params: [CaseTemplate]={}", updateCaseTemplateRequest.toString());
        try {
            CaseTemplate caseTemplate = caseTemplateMapper
                .toCaseTemplateByUpdateRequest(updateCaseTemplateRequest);
            updateCaseTemplate(caseTemplate);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplate!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    @LogRecord(operationType = EDIT, operationModule = CASE_TEMPLATE, template = "{{#caseTemplateList[0].name}}")
    public Boolean batchEdit(List<CaseTemplate> caseTemplateList) {
        log.info("CaseTemplateService-edit()-params: [caseTemplateList]={}", caseTemplateList.toString());
        try {
            for (CaseTemplate caseTemplate : caseTemplateList) {
                updateCaseTemplate(caseTemplate);
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("Failed to edit the CaseTemplate!", e);
            throw new ApiTestPlatformException(EDIT_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public Page<CaseTemplateResponse> page(CaseTemplateSearchRequest searchDto, ObjectId projectId) {
        try {
            return customizedCaseTemplateRepository.page(searchDto, projectId);
        } catch (Exception e) {
            log.error("Failed to search the CaseTemplate!", e);
            throw new ApiTestPlatformException(SEARCH_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public List<CaseTemplate> get(String groupId, String projectId) {
        try {
            CaseTemplate caseTemplate = CaseTemplate.builder().groupId(groupId).projectId(projectId).build();
            ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher(CommonFiled.PROJECT_ID.getFiled(), GenericPropertyMatchers.exact())
                .withMatcher(CommonFiled.GROUP_ID.getFiled(), GenericPropertyMatchers.exact())
                .withIgnoreNullValues();
            Example<CaseTemplate> example = Example.of(caseTemplate, exampleMatcher);
            return caseTemplateRepository.findAll(example);
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplate!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_ERROR);
        }
    }

    @Override
    public CaseTemplateDetailResponse getApiList(String caseTemplateId, boolean removed) {
        try {
            Optional<CaseTemplate> caseTemplate = caseTemplateRepository.findById(caseTemplateId);
            if (caseTemplate.isEmpty()) {
                throw new ApiTestPlatformException(GET_CASE_TEMPLATE_ERROR);
            }
            CaseTemplateResponse caseTemplateResponse = caseTemplateMapper.toDto(caseTemplate.get());
            List<CaseTemplateApiResponse> caseTemplateApiResponseList =
                caseTemplateApiService.listByCaseTemplateId(caseTemplateId, removed);
            return CaseTemplateDetailResponse.builder().caseTemplateResponse(caseTemplateResponse)
                .caseTemplateApiResponseList(caseTemplateApiResponseList).build();
        } catch (Exception e) {
            log.error("Failed to get the CaseTemplate detail!", e);
            throw new ApiTestPlatformException(GET_CASE_TEMPLATE_ERROR);
        }
    }

    private void updateCaseTemplate(CaseTemplate caseTemplate) {
        Optional<CaseTemplate> optionalSceneCase = caseTemplateRepository.findById(caseTemplate.getId());
        optionalSceneCase.ifPresent(sceneCaseFindById -> {
            caseTemplate.setCreateUserId(sceneCaseFindById.getCreateUserId());
            caseTemplate.setCreateDateTime(sceneCaseFindById.getCreateDateTime());
            if (!Objects.equals(caseTemplate.getRemoved(), sceneCaseFindById.getRemoved())) {
                editCaseTemplateApiStatus(caseTemplate, sceneCaseFindById.getRemoved());
            }
            caseTemplateRepository.save(caseTemplate);
        });
    }

    private void deleteCaseTemplateConn(List<CaseTemplateConn> caseTemplateConnList) {
        List<String> ids = caseTemplateConnList.stream().map(CaseTemplateConn::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
            caseTemplateConnService.deleteByIds(ids);
        }
    }

    private void deleteCaseTemplateApi(List<CaseTemplateApi> caseTemplateApiList) {
        List<String> ids = caseTemplateApiList.stream().map(CaseTemplateApi::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(ids)) {
            caseTemplateApiService.deleteByIds(ids);
        }
    }

    private void editCaseTemplateApiStatus(CaseTemplate caseTemplate, Boolean oldRemove) {
        List<CaseTemplateApi> caseTemplateApiList = caseTemplateApiService
            .getApiByCaseTemplateId(caseTemplate.getId(), oldRemove);
        if (CollectionUtils.isNotEmpty(caseTemplateApiList)) {
            for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
                caseTemplateApi.setRemoved(caseTemplate.getRemoved());
            }
            caseTemplateApiService.editAll(caseTemplateApiList);
        }
    }

    private void resetOrderAndIds(List<CaseTemplateApi> caseTemplateApiList,
        Map<String, Integer> apiConn, String caseTemplateId) {
        for (CaseTemplateApi caseTemplateApi : caseTemplateApiList) {
            caseTemplateApi.setOrder(apiConn.get(caseTemplateApi.getId()));
            caseTemplateApi.setCaseTemplateId(caseTemplateId);
            caseTemplateApi.setId(null);
        }
    }

    private void setCaseTemplateId(List<CaseTemplateApi> sceneCaseApiCaseTemplate, String caseTemplateId) {
        for (CaseTemplateApi caseTemplateApi : sceneCaseApiCaseTemplate) {
            caseTemplateApi.setCaseTemplateId(caseTemplateId);
        }
    }
}
